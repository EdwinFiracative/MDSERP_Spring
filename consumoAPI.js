// =========================================================================
// 1. MENÚ PRINCIPAL E INICIALIZACIÓN
// =========================================================================
/**
 * Función especial de Google Apps Script que se ejecuta automáticamente al abrir el documento.
 * Crea un menú personalizado en la barra superior de la hoja de cálculo.
 */
function onOpen() {
    SpreadsheetApp.getUi()
        .createMenu("Administrar") // Nombre del menú principal
        .addItem("Actualizar desde ERP", "actualizarHojasDesdeApi") // Opción que dispara la función principal
        .addToUi();
}

// =========================================================================
// 2. CONFIGURACIÓN CENTRALIZADA DE ENDPOINTS Y MAPEO DE HOJAS
// =========================================================================
/**
 * Matriz de configuración (Array de objetos) que define el comportamiento para cada hoja.
 * Cada objeto contiene:
 * - hoja: El nombre exacto de la pestaña en el Google Sheet.
 * - endpoint: El sub-path de la API a consultar.
 * - queryParams: Parámetros adicionales para la URL (filtros, orden, etc.).
 * - filaInicio: Indica desde qué fila se empezarán a escribir los datos (útil para respetar encabezados fijos).
 * - formatos: (Opcional) Función callback para aplicar formatos de número o fecha específicos a columnas.
 */
const CONFIG_SINCRONIZACION = [
    {
        hoja: "Inventario Doblados Linea",
        endpoint: "/api/api/inv-neto-sql", // Ajustar sub-path si difiere
        //queryParams: "?filtro=doblados",   // Manejo de lógica equivalente al WHERE
        queryParams: "",
        filaInicio: 3, // 1 si genera encabezados dinámicos, 2 si la plantilla ya los tiene
        formatos: (sheet) => {
            sheet.getRange("A:A").setNumberFormat("@"); // Formato texto
        }
    },
    {
        hoja: "Op_Proceso",
        endpoint: "/api/api/co-op-proceso",
        queryParams: "",
        filaInicio: 2,
        formatos: (sheet) => {
            sheet.getRange("B:B").setNumberFormat("dd/MM/yyyy");
            sheet.getRange("C:C").setNumberFormat("@");
            sheet.getRange("K:K").setNumberFormat("dd/MM/yyyy");
            sheet.getRange("L:N").setNumberFormat("$0.00");
            sheet.getRange("W:W").setNumberFormat("$0.00");
        }
    },
    {
        hoja: "Pedidos",
        endpoint: "/api/api/pedidos-pend",
        queryParams: "",
        filaInicio: 2,
        formatos: (sheet) => {
            sheet.getRange("A:A").setNumberFormat("yyyy-MM-dd");
            sheet.getRange("J:J").setNumberFormat("@");
            sheet.getRange("I:I").setNumberFormat("dd/MM/yyyy");
        }
    },
    /*{
      hoja: "Comercializacion",
      endpoint: "/api/api/inv-neto-sql",
      //queryParams: "?linea=2E",
      queryParams: "",
      filaInicio: 2,
      formatos: (sheet) => {
        sheet.getRange("A:A").setNumberFormat("@");
      }
    },
    {
      hoja: "Gabinetes Poliester con Bandeja",
      endpoint: "/api/api/inv-neto-sql",
      //queryParams: "?linea=SE2E",
      queryParams: "",
      filaInicio: 2,
      formatos: (sheet) => {
        sheet.getRange("A:A").setNumberFormat("@");
      }
    },*/
    {
        hoja: "Cajas Especiales",
        endpoint: "/api/api/especiales",
        queryParams: "",
        filaInicio: 2
    },
    {
        hoja: "Tuberia",
        endpoint: "/api/api/tuberia",
        queryParams: "",
        filaInicio: 2
    },
    {
        hoja: "OP_PROCESO_FABRICUR",
        endpoint: "/api/api/op-fabricur",
        queryParams: "",
        filaInicio: 2,
        formatos: (sheet) => {
            sheet.getRange("A:A").setNumberFormat("@");
        }
    },
    /*{
      hoja: "Maestro",
      endpoint: "/api/api/catalogo-prod",
      //queryParams: "?negocio=Product",
      queryParams: "",
      filaInicio: 1
    }*/
];

// =========================================================================
// 3. CORE DE AUTENTICACIÓN OAUTH2 / OPENID CONNECT
// =========================================================================
/**
 * PASO 1: Obtención del Token de Acceso.
 * Realiza una petición POST al servidor Keycloak (OIDC) para autenticar al cliente.
 * 
 * @return {string} Access Token JWT para ser usado en las cabeceras de las peticiones a la API.
 */
function obtenerAccessToken() {
    const tokenUrl = 'https://erp.proelectricos.com/auth/realms/MDSERP/protocol/openid-connect/token';
    
    // Credenciales y tipo de concesión (grant_type) para la autenticación
    const payload = {
        client_id: 'mds-erp-front',
        grant_type: 'password',
        username: '80037690',
        password: '*Al1980eF#' // Nota: Se recomienda usar ScriptProperties para no exponer claves
    };

    const opciones = {
        method: 'post',
        payload: payload,
        muteHttpExceptions: true
    };

    try {
        const respuesta = UrlFetchApp.fetch(tokenUrl, opciones);
        const codigoRespuesta = respuesta.getResponseCode();
        const cuerpo = respuesta.getContentText();

        if (codigoRespuesta !== 200) {
            throw new Error(`HTTP ${codigoRespuesta}: ${cuerpo}`);
        }

        // Retorna solo el token de acceso del JSON de respuesta
        return JSON.parse(cuerpo).access_token;
    } catch (error) {
        throw new Error(`Fallo en autenticación OIDC: ${error.message}`);
    }
}

// =========================================================================
// 4. PROCESAMIENTO PRINCIPAL ORQUESTADO
// =========================================================================
/**
 * PASO CENTRAL: Orquestación de la Sincronización.
 * Esta función recorre la configuración, obtiene datos de la API y actualiza el Google Sheet.
 */
function actualizarHojasDesdeApi() {
    const scriptStartTime = Date.now(); // Marca de tiempo inicial para control de timeouts
    const fechaActual = new Date();
    const diaSemana = fechaActual.getDay();
    const hora = fechaActual.getHours();

    console.log(`Ejecución: ${fechaActual.toTimeString()} | Día: ${diaSemana} | Hora: ${hora}`);

    // PASO 2: Guardas Operacionales.
    // Evita ejecuciones innecesarias en días no laborales o fuera de horario de oficina.
    if (diaSemana === 0) {
        console.log("Sincronización omitida: Domingo no laboral.");
        return;
    }
    if (hora < 5 || hora > 18) {
        console.log("Sincronización omitida: Fuera de horario operativo (5:00 - 18:00).");
        return;
    }

    const spreadsheet = SpreadsheetApp.getActiveSpreadsheet();
    spreadsheet.toast("Iniciando consumo masivo de APIs...", "Estado", 2);

    const baseApiUrl = 'https://erp.proelectricos.com';

    // PASO 3: Iteración sobre cada configuración de hoja definida en CONFIG_SINCRONIZACION.
    for (let i = 0; i < CONFIG_SINCRONIZACION.length; i++) {
        const config = CONFIG_SINCRONIZACION[i];

        // Control de tiempo para no exceder los 6 minutos permitidos por Google Apps Script.
        if (Date.now() - scriptStartTime > 600000) { // 10 minutos (aunque el límite suele ser 6, se usa como referencia)
            console.warn("⏳ Tiempo límite de ejecución de GAS cercano. Abortando limpiamente.");
            Browser.msgBox(`⚠️ Sincronización parcial. Se alcanzaron a actualizar las hojas hasta: ${CONFIG_SINCRONIZACION[i-1].hoja}. Ejecute nuevamente.`);
            break;
        }

        // PASO 4: Autenticación por cada endpoint (para asegurar token fresco).
        let accessToken;
        try {
            console.log(`🔐 Solicitando token para: [${config.hoja}]...`);
            accessToken = obtenerAccessToken();
        } catch (err) {
            console.error(`❌ Error de Autenticación para ${config.hoja}: ${err.message}`);
            continue; // Salta a la siguiente hoja si falla el login
        }

        // PASO 5: Localización de la pestaña en el libro.
        let sheet = spreadsheet.getSheetByName(config.hoja);
        if (!sheet) {
            console.log(`⚠️ Hoja '${config.hoja}' no parametrizada en el libro actual. Saltando...`);
            continue;
        }

        const urlCompleta = `${baseApiUrl}${config.endpoint}${config.queryParams}`;
        console.log(`📥 Consumiendo: [${config.hoja}] -> URL: ${urlCompleta}`);

        try {
            // PASO 6: Petición GET a la API con el Bearer Token.
            const apiResponse = UrlFetchApp.fetch(urlCompleta, {
                method: 'get',
                headers: {
                    'Authorization': `Bearer ${accessToken}`,
                    'ngrok-skip-browser-warning': 'true', // Omitir avisos de túneles si aplica
                    'Accept': 'application/json'
                },
                muteHttpExceptions: true
            });

            const httpCode = apiResponse.getResponseCode();
            if (httpCode !== 200) {
                console.error(`❌ Error HTTP ${httpCode} en endpoint de ${config.hoja}. Saltando pestaña.`);
                continue;
            }

            // PASO 7: Parseo de datos JSON y validación de contenido.
            const rawData = JSON.parse(apiResponse.getContentText());
            if (!Array.isArray(rawData) || rawData.length === 0) {
                console.log(`ℹ️ No se retornaron registros para la hoja: ${config.hoja}`);
                continue;
            }

            // PASO 8: Procesamiento de Encabezados (Headers).
            // Convierte camelCase o snake_case de la API a nombres más legibles en mayúsculas.
            const keys = Object.keys(rawData[0]);
            const headers = keys.map(key => {
                return key.replace(/([A-Z])/g, ' $1').replace(/[-_]/g, ' ').toUpperCase().trim();
            });

            // PASO 9: Preparación de la Matriz de Datos (Filas).
            // Se limpian nulos y se convierten objetos complejos a strings JSON.
            const rows = rawData.map(item => keys.map(key => {
                let valor = item[key];
                if (valor === null || valor === undefined) return "";
                return (typeof valor === 'object') ? JSON.stringify(valor) : valor;
            }));

            // PASO 10: Escritura Atómica en la Hoja de Cálculo.
            const lastRow = sheet.getLastRow();
            const lastCol = sheet.getLastColumn();

            // Limpieza previa de los datos antiguos (preservando lo que esté por encima de filaInicio)
            if (lastRow >= config.filaInicio && lastCol > 0) {
                sheet.getRange(config.filaInicio, 1, lastRow, lastCol).clearContent();
            }

            let destinoFilaDatos = config.filaInicio;
            
            // Si la configuración indica que empezamos en la fila 1, escribimos también encabezados.
            if (config.filaInicio === 1) {
                sheet.getRange(1, 1, 1, headers.length).setValues([headers]);
                sheet.getRange("1:1").setFontWeight("bold");
                destinoFilaDatos = 2; // Los datos empiezan en la 2
            }

            // Inserción de todos los datos de golpe (eficiencia V8)
            if (rows.length > 0) {
                sheet.getRange(destinoFilaDatos, 1, rows.length, headers.length).setValues(rows);
            }

            // PASO 11: Aplicación de formatos personalizados (fechas, monedas, etc.).
            if (typeof config.formatos === 'function') {
                config.formatos(sheet);
            }

            spreadsheet.toast(`Hoja [${config.hoja}] sincronizada con éxito.`, "Progreso", 2);
            SpreadsheetApp.flush(); // Actualiza la interfaz de usuario para mostrar cambios inmediatos

        } catch (errorPeticion) {
            console.error(`❌ Error crítico al procesar la hoja ${config.hoja}: ${errorPeticion.message}`);
        }
    }

    spreadsheet.toast("¡Sincronización completa desde API!", "Éxito", 4);
}