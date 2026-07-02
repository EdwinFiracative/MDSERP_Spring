/*// =========================================================================
// 1. MENÚ PRINCIPAL E INICIALIZACIÓN
// =========================================================================
function onOpen() {
  SpreadsheetApp.getUi()
    .createMenu("Administrar")
    .addItem("Actualizar desde ERP", "actualizarHojasDesdeApi")
    .addToUi();
}

// =========================================================================
// 2. CONFIGURACIÓN CENTRALIZADA DE ENDPOINTS Y MAPEO DE HOJAS
// =========================================================================
/**
 * Define la estructura de cada sincronización.
 * Reemplaza los paths de los endpoints según la especificación de tu API interna.
 */
const CONFIG_SINCRONIZACION = [
    {
        hoja: "Inventario Doblados Linea",
        endpoint: "/api/api/inv-neto-sql", // Ajustar sub-path si difiere
        //queryParams: "?filtro=doblados",   // Manejo de lógica equivalente al WHERE
        queryParams: "",
        filaInicio: 3, // 1 si genera encabezados dinámicos, 2 si la plantilla ya los tiene
        formatos: (sheet) => {
            sheet.getRange("A:A").setNumberFormat("@");
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
    {
        hoja: "Comercializacion",
        endpoint: "/api/api/inv-neto-sql",
        //queryParams: "?linea=2E",
        queryParams: "",
        filaInicio: 1,
        formatos: (sheet) => {
            sheet.getRange("A:A").setNumberFormat("@");
        }
    },
    {
        hoja: "Gabinetes Poliester con Bandeja",
        endpoint: "/api/api/inv-neto-sql",
        //queryParams: "?linea=SE2E",
        queryParams: "",
        filaInicio: 1,
        formatos: (sheet) => {
            sheet.getRange("A:A").setNumberFormat("@");
        }
    },
    {
        hoja: "Cajas Especiales",
        endpoint: "/api/api/especiales",
        queryParams: "",
        filaInicio: 1
    },
    {
        hoja: "Tuberia",
        endpoint: "/api/api/tuberia",
        queryParams: "",
        filaInicio: 1
    },
    {
        hoja: "OP_PROCESO_FABRICUR",
        endpoint: "/api/api/op-fabricur",
        queryParams: "",
        filaInicio: 1,
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
 * Obtiene el access_token del servidor Keycloak/OIDC de forma resiliente.
 * @return {string} Access Token válido.
 */
function obtenerAccessToken() {
    const tokenUrl = 'https://erp.proelectricos.com/auth/realms/MDSERP/protocol/openid-connect/token';
    const payload = {
        client_id: 'mds-erp-front',
        grant_type: 'password',
        username: '80037690',
        password: '*Al1980eF#' // Recomiendo migrar esto a ScriptProperties por seguridad
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

        return JSON.parse(cuerpo).access_token;
    } catch (error) {
        throw new Error(`Fallo en autenticación OIDC: ${error.message}`);
    }
}

// =========================================================================
// 4. PROCESAMIENTO PRINCIPAL ORQUESTADO
// =========================================================================
function actualizarHojasDesdeApi() {
    const scriptStartTime = Date.now();
    const fechaActual = new Date();
    const diaSemana = fechaActual.getDay();
    const hora = fechaActual.getHours();

    console.log(`Ejecución: ${fechaActual.toTimeString()} | Día: ${diaSemana} | Hora: ${hora}`);

    // Guardas operacionales de control de horario corporativo
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

    let accessToken;
    try {
        console.log("🔐 Solicitando token centralizado al servidor OIDC...");
        accessToken = obtenerAccessToken();
    } catch (err) {
        console.error(err.message);
        Browser.msgBox(`Error de Autenticación: ${err.message}`);
        return;
    }

    const baseApiUrl = 'https://erp.proelectricos.com';

    // Iteración sobre la matriz de configuración
    for (let i = 0; i < CONFIG_SINCRONIZACION.length; i++) {
        const config = CONFIG_SINCRONIZACION[i];

        // Freno de seguridad anti-timeout (Límite preventivo a los 5 minutos para evitar los 6 minutos de GAS)
        if (Date.now() - scriptStartTime > 300000) {
            console.warn("⏳ Tiempo límite de ejecución de GAS cercano (5 min). Abortando limpiamente.");
            Browser.msgBox(`⚠️ Sincronización parcial. Se alcanzaron a actualizar las hojas hasta: ${CONFIG_SINCRONIZACION[i-1].hoja}. Ejecute nuevamente.`);
            break;
        }

        let sheet = spreadsheet.getSheetByName(config.hoja);
        if (!sheet) {
            console.log(`⚠️ Hoja '${config.hoja}' no parametrizada en el libro actual. Saltando...`);
            continue;
        }

        const urlCompleta = `${baseApiUrl}${config.endpoint}${config.queryParams}`;
        console.log(`📥 Consumiendo: [${config.hoja}] -> URL: ${urlCompleta}`);

        try {
            const apiResponse = UrlFetchApp.fetch(urlCompleta, {
                method: 'get',
                headers: {
                    'Authorization': `Bearer ${accessToken}`,
                    'ngrok-skip-browser-warning': 'true',
                    'Accept': 'application/json'
                },
                muteHttpExceptions: true
            });

            const httpCode = apiResponse.getResponseCode();
            if (httpCode !== 200) {
                console.error(`❌ Error HTTP ${httpCode} en endpoint de ${config.hoja}. Saltando pestaña.`);
                continue;
            }

            const rawData = JSON.parse(apiResponse.getContentText());
            if (!Array.isArray(rawData) || rawData.length === 0) {
                console.log(`ℹ️ No se retornaron registros para la hoja: ${config.hoja}`);
                continue;
            }

            // Extracción y saneamiento dinámico de esquema de datos (Headers)
            const keys = Object.keys(rawData[0]);
            const headers = keys.map(key => {
                return key.replace(/([A-Z])/g, ' $1').replace(/[-_]/g, ' ').toUpperCase().trim();
            });

            // Mapeo bidimensional nativo optimizado para V8 (Sanitizado de nulos y objetos internos)
            const rows = rawData.map(item => keys.map(key => {
                let valor = item[key];
                if (valor === null || valor === undefined) return "";
                return (typeof valor === 'object') ? JSON.stringify(valor) : valor;
            }));

            // Operación atómica de escritura en Google Sheets (Limpia y Sobrescribe de un solo golpe)
            const lastRow = sheet.getLastRow();
            const lastCol = sheet.getLastColumn();

            if (lastRow >= config.filaInicio && lastCol > 0) {
                // Limpiamos los datos previos respetando la fila de inicio configurada
                sheet.getRange(config.filaInicio, 1, lastRow, lastCol).clearContent();
            }

            // Si la fila de inicio es 1, inyectamos los encabezados limpios de la API
            let destinoFilaDatos = config.filaInicio;
            if (config.filaInicio === 1) {
                sheet.getRange(1, 1, 1, headers.length).setValues([headers]);
                sheet.getRange("1:1").setFontWeight("bold");
                destinoFilaDatos = 2;
            }

            // Escritura en bloque de la matriz de datos procesada
            if (rows.length > 0) {
                sheet.getRange(destinoFilaDatos, 1, rows.length, headers.length).setValues(rows);
            }

            // Aplicación de formatos estéticos condicionales/específicos delegados por configuración
            if (typeof config.formatos === 'function') {
                config.formatos(sheet);
            }

            spreadsheet.toast(`Hoja [${config.hoja}] sincronizada con éxito.`, "Progreso", 2);
            SpreadsheetApp.flush(); // Fuerza el vaciado del búfer gráfico de la UI de Sheets

        } catch (errorPeticion) {
            console.error(`❌ Error crítico al procesar la hoja ${config.hoja}: ${errorPeticion.message}`);
        }
    }

    spreadsheet.toast("¡Sincronización completa desde API!", "Éxito", 4);
}