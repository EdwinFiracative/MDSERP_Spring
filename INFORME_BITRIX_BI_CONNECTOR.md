# Informe de ejecución: conector BI Builder de Bitrix24 para MDSERP (Spring Boot)

> **Para Claude Code.** Este documento es la especificación completa de la tarea. Las decisiones ya están tomadas (sección 2); no las vuelvas a preguntar. Ejecuta las fases en orden (sección 6), cumple los criterios de aceptación de cada una y detente solo en los puntos marcados **⛔ PARAR Y PREGUNTAR**.
>
> Proyecto: `C:\Users\C-016\Documents\java\MDSERP_Spring` — fecha de la información: 2026-10-20.

---

## 1. Objetivo

Publicar los pedidos de venta del ERP (ErpDb) en **Bitrix24 BI Builder** mediante un **conector BI** (opción C) implementado dentro del backend Spring Boot existente. Bitrix24 consulta al backend cuando necesita datos; **no se guarda nada en el CRM** y **no hay sincronización programada**.

## 2. Decisiones cerradas

| Tema | Decisión |
|---|---|
| Opción de integración | **C – Conector BI** (no Smart Process, no app OAuth). |
| Uso de los datos | Solo BI (tableros y reportes en BI Builder). No se crean entidades en el CRM. |
| Smart Process | No existe ninguno para pedidos y **no se crea**. No hay `entityTypeId`. |
| Exposición a internet | **Sin túnel Cloudflare.** Se reutiliza el nginx existente de `erp.proelectricos.com` (HTTPS válido). |
| Autenticación con Bitrix24 | Webhook entrante existente (`BITRIX_WEBHOOK_URL`, scopes incluyen `biconnector`). Solo se usa para registrar el conector. |
| Granularidad | **Una fila por línea de pedido** (`orderReference`), con los datos de cabecera repetidos. |
| Línea de negocio | Se usa la de la **línea** (`orderReferBusinessLine`). El arreglo `orderHeaderBusinessLine` de la cabecera **se descarta** (duplicaría pedidos con varias líneas de negocio). |
| Notas | Tabla aparte (`pedidos_notas`), no en la tabla de líneas. |
| Cancelaciones | No se borra nada: se expone el estado de la línea y BI filtra. |
| Frecuencia / latencia | Consulta en vivo bajo demanda. La actualización de cada 12 h la gestiona la programación del dataset en BI Builder, no el backend. |
| Notificación de errores | Correo a `investigacionydesarrollo3@proelectricos.com`. |
| Restricciones de nube | Ninguna declarada por el usuario (ver nota de datos personales en 8). |

## 3. Entorno técnico (verificado)

- **Java 17** (JDK 17.0.12), **Spring Boot 3.5.5** (Spring 6.2.x), **Maven 3.9.11** con wrapper (`mvnw` / `mvnw.cmd`). No hay Gradle.
- Clientes HTTP existentes:
  - `RestClient` → bean `bitrixRestClient` en `src/main/java/com/proelectricos/mdserp/config/BitrixConfig.java:28`, usado por `service/bitrix/BitrixService.java:54`. **Sin timeouts de conexión ni de lectura.**
  - OkHttp (vía `telegrambots-client` 10.3.0, `chatbot/config/ChatbotConfig.java:36`) y Google GenAI SDK 1.75.0.
  - No hay RestTemplate, WebClient, Feign, Retrofit ni `@HttpExchange`.
- Resiliencia: **ninguna** (sin Resilience4j ni Spring Retry). Si se añade: `resilience4j-spring-boot3` + `spring-boot-starter-aop`.
- Despliegue: **un solo contenedor Docker** (`mdsback-container`, puerto 8181:8181, `--restart unless-stopped`, volumen `mdserp-logs`) en un servidor Linux remoto, contexto Docker `linux-mds-server` (`dockerdeploy.ps1`). El JAR se compila en Windows (`target/MDSERP-1.0.0-SNAPSHOT.jar`).
- Delante: **nginx/1.24.0 (Ubuntu)** en `erp.proelectricos.com` (IP 186.28.243.189). Nginx **quita el primer `/api`**: la ruta pública `/api/api/orderheader` llega a Spring como `/api/orderheader`.
- Keycloak en el mismo dominio: realm `MDSERP`. Seguridad del backend: JWT stateless.
- SQL Server 2014 Standard (sin CDC; Change Tracking desactivado): `ErpDb` en `186.28.243.188\sqlfactory:63138`; tablas `OrderHeader`, `OrderReference`, `OrderNote`.
- Salida HTTPS a internet: directa, sin proxy (por evidencia; no comprobado desde el servidor).
- Bitrix24: `https://proelectricos.bitrix24.es`, licencia `la_pro100` (Professional). Límite estimado 2 req/s con ráfagas de 50.

## 4. Datos de origen

### 4.1 Endpoint existente (referencia)

```
GET /api/orderheader?fechaInicial=yyyy-MM-dd&fechaFinal=yyyy-MM-dd[&estados=1,2]
(público: https://erp.proelectricos.com/api/api/orderheader)
Authorization: Bearer <token Keycloak, client_credentials, cliente "Bitrix", dura 300 s>
```

- Ambas fechas obligatorias e inclusivas. `estados` = ids de `OrderReferStatus` separados por coma.
- **Sin paginación** (~3,6 KB por pedido; ~2 MB por mes).
- Respuesta `application/json` **sin charset**: decodificar siempre como UTF-8.

> El conector vive en el mismo backend, así que **debe reutilizar en proceso el servicio/repositorio que alimenta `/api/orderheader`** (o una consulta/vista SQL propia), no llamarse a sí mismo por HTTP con OAuth. Las variables `LOCAL_SOURCE_*` solo se usan si se decide lo contrario.

### 4.2 Esquema (resumen)

Arreglos nunca nulos (vienen `[]`). Fechas `yyyy-MM-dd` sin hora ni zona. Importes en **COP**, decimales con 4 cifras → usar `BigDecimal`, nunca `float/double` en el mapeo interno.

| Campo origen | Tipo | Nulo | Notas |
|---|---|---|---|
| `orderHeaderId` | long | No | Único por pedido |
| `orderHeaderNumber` | int | No | Clave de negocio del pedido |
| `orderHeaderDate` | date | No | |
| `orderHeaderPaymeConditions` | string(11) | No | Texto libre sucio (ver 4.4) |
| `orderHeaderDescription` | string(~104) | **Sí** | |
| `orderHeaderBranch.branchId / branchCode / branchCity / branchAddress` | int / string(2) / string / string | No | Ciudad puede venir truncada (`SOACHA (CUNDINAMARCA`) |
| `…branchClient.clientId` | int | No | |
| `…clientThirdParty.thirdPartyIdentNumber / thirdPartyName` | long / string(60) | No | NIT o cédula; razón social |
| `orderHeaderVendor.vendorId / vendorCode` | int / string(5) | No | Si no hay vendedor, viene el de la sede |
| `…vendorThirdParty.thirdPartyIdentNumber / thirdPartyName` | long / string | No | |
| `orderReference[].orderReferId` | int | No | Único por línea |
| `…orderReferPosition` | int | No | Nº de línea en el pedido |
| `…orderReferReference.referId / referCod / referCod2` | int / string(11) / string | No / No / **Sí** (siempre null) | Producto |
| `…referName` | string(175) | No | **Puede traer `\n`** → normalizar a espacio |
| `…referDescription` | string | **Sí** | |
| `…referMeasuUnit.measuUnitId / measuUnitName` | int / string | No | |
| `…orderReferBusinessLine.referClassId / referClassName` | int / string | **Sí** | Nulo si el producto no está clasificado |
| `…orderReferQuantity` | int | No | |
| `…orderReferUnitPrice` | decimal(4) | No | COP |
| `…valorTotal` | decimal(4) | No | Cantidad × precio, **sin impuestos**; calculado por el backend |
| `…orderReferDelivDate` | date | No | |
| `…orderReferProject.projeId / projeName` | int / string | **Sí** (428/443) | |
| `…orderReferStatus.orderReferStatusId / Name` | int / string | No | |
| `orderNote[].orderNoteId / orderNotePosition / orderNoteText` | int / int / string | arreglo puede venir vacío | |

### 4.3 Claves naturales (idempotencia)

| Nivel | Clave | Restricción en BD |
|---|---|---|
| Pedido | `orderHeaderNumber` | `UQ_OrderHeader_OrderHeaderNumber` |
| Línea | `orderHeaderNumber + orderReferPosition` | `UQ_OrderReference_OrderHeader_Position` |
| Nota | `orderHeaderNumber + orderNotePosition` | `UQ_OrderNote_OrderHeader_Position` |

### 4.4 Enumeraciones

- **Estado de línea**: 1 Cancelado · 2 Aprobado · 3 Detenido · 4 En Revisión · 5 Anulado (lista completa en `/api/orderreferstatus`).
- **Línea de negocio**: 1152 conduit · 1153 comercialización · 1154 celdas y tableros · 1155 MDS Ingeniería · 1159 ingresos no operacionales.
- **Unidad de medida** (observadas): 1 BOLSA · 4 CONJUNTO · 15 PIEZA.
- **Condición de pago**: texto libre con variantes (`CREDITO  VS` y `CREDITO VS` son lo mismo). Exponer el valor original **y** una columna normalizada (`trim` + colapsar espacios múltiples + mayúsculas).

### 4.5 Volumen

- Backfill (ErpDb, a 2026-10-20): **48.418 pedidos · 229.630 líneas · 49.966 notas**, de 2016-02-10 a 2026-10-20.
- 4.000–6.000 pedidos/año (17.000–31.000 líneas). Promedio diario: ~14 pedidos / ~66 líneas (pico 30 / 215).
- Sin columna de última modificación ni rowversion en ErpDb → **los cambios se recogen reconsultando una ventana móvil por fecha** (p. ej. 60–90 días). No hace falta delta.
- Borrados en el sistema de origen no llegan a ErpDb (no hay trigger de DELETE). Fuera de alcance.

## 5. Diseño del conector

### 5.1 Tablas expuestas a BI Builder

**`pedidos_lineas`** (una fila por `orderReference`)

| Columna | Tipo BI | Origen |
|---|---|---|
| `id_linea` | int | `orderReferId` |
| `clave_linea` | string | `"{orderHeaderNumber}-{orderReferPosition}"` |
| `id_pedido` | int | `orderHeaderId` |
| `numero_pedido` | int | `orderHeaderNumber` |
| `fecha_pedido` | date | `orderHeaderDate` |
| `condicion_pago` | string | original |
| `condicion_pago_norm` | string | normalizada (4.4) |
| `descripcion_pedido` | string | `orderHeaderDescription` |
| `sede_id`, `sede_codigo`, `sede_ciudad`, `sede_direccion` | int/string | `orderHeaderBranch.*` |
| `cliente_id`, `cliente_nit`, `cliente_nombre` | int/string/string | `branchClient.*` (NIT como string) |
| `vendedor_id`, `vendedor_codigo`, `vendedor_nombre` | int/string/string | `orderHeaderVendor.*` |
| `posicion` | int | `orderReferPosition` |
| `producto_id`, `producto_codigo`, `producto_nombre`, `producto_descripcion` | int/string | `orderReferReference.*` (`\n` → espacio) |
| `unidad_medida` | string | `measuUnitName` |
| `linea_negocio_id`, `linea_negocio` | int/string | `orderReferBusinessLine.*` (nullable) |
| `cantidad` | int | `orderReferQuantity` |
| `precio_unitario` | double | `orderReferUnitPrice` |
| `valor_total` | double | `valorTotal` |
| `fecha_entrega` | date | `orderReferDelivDate` |
| `proyecto_id`, `proyecto` | int/string | `orderReferProject.*` (nullable) |
| `estado_id`, `estado` | int/string | `orderReferStatus.*` |

**`pedidos_notas`**: `numero_pedido`, `id_nota`, `posicion`, `texto`.

### 5.2 Contrato con Bitrix24

⚠️ **Antes de escribir código, verifica en la documentación oficial** (`https://apidocs.bitrix24.com`, sección *BI Builder → Connectors*: `biconnector.connector.add`, `biconnector.source.add`, `biconnector.dataset.add`) el contrato exacto: nombres de los campos `urlCheck`, `urlTableList`, `urlTableDescription`, `urlData`, el formato de petición (cuerpo, `select`, `filter`, `limit`), los tipos de columna admitidos y el formato de respuesta. Implementa exactamente lo que diga la documentación; la tabla siguiente es orientativa.

| Endpoint Spring (interno) | URL pública | Función |
|---|---|---|
| `POST /bi-connector/check` | `{B24_APP_PUBLIC_BASE_URL}/bi-connector/check` | Validar clave y responder OK |
| `POST /bi-connector/table_list` | `…/table_list` | Listar `pedidos_lineas`, `pedidos_notas` |
| `POST /bi-connector/table_description` | `…/table_description` | Columnas y tipos de una tabla |
| `POST /bi-connector/data` | `…/data` | Filas según `select`/`filter`/`limit` |

Con `B24_APP_PUBLIC_BASE_URL=https://erp.proelectricos.com/api`, nginx quita `/api` y Spring recibe `/bi-connector/...`. Comprueba que el `location` de nginx ya cubre esa ruta; si no, **⛔ PARAR Y PREGUNTAR** (el nginx no está en el repo).

### 5.3 Reglas del endpoint `data`

- Si el filtro trae rango de `fecha_pedido`, úsalo. **Si no trae rango, aplica una ventana por defecto** (`B24_CONNECTOR_DEFAULT_WINDOW_DAYS`, por defecto 90) para no devolver los 229.630 registros de golpe.
- Consulta por bloques de fechas (p. ej. mensuales) y **escribe la respuesta en streaming** para no cargar todo en memoria.
- Respeta `limit` si viene. Orden estable: `numero_pedido, posicion`.
- No filtrar cancelados en el backend: BI decide con `estado_id`.
- Respuesta en UTF-8 explícito (`application/json;charset=UTF-8`).

### 5.4 Seguridad

- Las rutas `/bi-connector/**` quedan fuera del JWT de Keycloak (`permitAll` en la `SecurityFilterChain`), **pero** se validan con una clave compartida: el conector se registra con un *setting* de tipo clave y el backend la compara (comparación en tiempo constante) con `B24_CONNECTOR_KEY`. Sin clave válida → 401.
- Ninguna otra ruta debe quedar abierta por este cambio.
- No registrar en logs la clave ni el cuerpo completo de las respuestas.

## 6. Plan de ejecución

### Fase 0 – Higiene de secretos (antes que nada)

1. En `src/main/resources/application.properties`:
   - Línea 60: `bitrix.webhook-url=${BITRIX_WEBHOOK_URL:}` (hoy tiene la URL con token en claro; en local Spring no mapea la variable del `.env`).
   - Líneas 7 y 13 (contraseñas SQL Server): pasar a `${VARIABLE:}` y añadir las variables al `.env`.
2. Confirmar que `.env` está en `.gitignore`.
3. **No** reescribas el historial de git ni rotes credenciales tú: deja anotado al usuario qué rotar (sección 8).

✅ Aceptación: `git grep` no encuentra tokens ni contraseñas en archivos versionados; la app arranca en local leyendo `.env`.

### Fase 1 – Robustez del cliente Bitrix

1. Añadir timeouts explícitos al `bitrixRestClient` (`BitrixConfig.java:28`): conexión 5 s, lectura 30 s (configurables por propiedad).
2. (Opcional) Reintento con backoff para errores 5xx/429 al llamar a Bitrix.

✅ Aceptación: test que verifica que el cliente tiene timeouts configurados.

### Fase 2 – Capa de datos aplanada

1. Localizar el servicio/repositorio que construye la respuesta de `/api/orderheader`.
2. Crear `BiOrderLineRow` y `BiOrderNoteRow` (records) y un servicio `BiOrderDatasetService` que, dado un rango de fechas, devuelva las filas de 5.1 (reutilizando la consulta existente o una vista SQL nueva).
3. Normalizaciones: `\n` en `producto_nombre`, `condicion_pago_norm`, NIT como string, decimales como `BigDecimal` internamente.
4. Si propones crear una vista SQL o cualquier DDL en ErpDb: déjala como script en `/sql` con prueba `BEGIN TRAN … ROLLBACK` y **⛔ PARAR Y PREGUNTAR** antes de ejecutarla.

✅ Aceptación: test unitario con el JSON de ejemplo (anexo A) que produce exactamente 1 fila de línea y 1 nota con los valores esperados; para el rango 2026-10-19 → 2026-10-19 el número de filas coincide con la suma de `orderReference` del endpoint existente.

### Fase 3 – Endpoints del conector

1. `BiConnectorController` con los 4 endpoints de 5.2, según el contrato verificado en la documentación.
2. Filtro/validador de `B24_CONNECTOR_KEY` y ajuste de la `SecurityFilterChain`.
3. Streaming y ventana por defecto (5.3).

✅ Aceptación: tests de controlador (MockMvc) para: clave inválida → 401; `table_list` devuelve las 2 tablas; `table_description` devuelve las columnas de 5.1; `data` con rango devuelve las filas esperadas y respeta `limit`.

### Fase 4 – Registro del conector en Bitrix24

1. Script o comando de administración (no en el arranque de la app) que llama por el webhook a `biconnector.connector.add` con las URLs públicas, título "MDSERP Pedidos" y el setting de clave.
2. Crear la fuente (`biconnector.source.add`) y los datasets (`biconnector.dataset.add`) si la API lo permite; si no, documentar los pasos manuales en BI Builder.
3. **⛔ PARAR Y PREGUNTAR** antes de ejecutar cualquier llamada que escriba en el portal de Bitrix24 de producción.

✅ Aceptación: el conector aparece en BI Builder, la conexión "check" pasa y el dataset `pedidos_lineas` muestra datos.

### Fase 5 – Errores y notificación

1. Manejo de excepciones en el controlador: log estructurado + respuesta de error con el formato que espere Bitrix.
2. Aviso por correo a `investigacionydesarrollo3@proelectricos.com` ante fallos (con limitación para no enviar un correo por petición, p. ej. máximo 1 cada 30 min). Si el proyecto no tiene `spring-boot-starter-mail` ni SMTP configurado, añade las variables `MAIL_*` al `.env` de ejemplo y **⛔ PARAR Y PREGUNTAR** por el servidor SMTP.

### Fase 6 – Despliegue

1. Cambiar la imagen base del `Dockerfile` de `openjdk:17-ea-17-jdk-oracle` (early access, deprecada) a `eclipse-temurin:17-jre`.
2. Añadir las variables nuevas al `.env` del servidor (solo nombres en el repo).
3. Desplegar con `dockerdeploy.ps1`. **⛔ PARAR Y PREGUNTAR** antes de desplegar a producción.

✅ Aceptación: `curl -X POST https://erp.proelectricos.com/api/bi-connector/check` sin clave → 401; con clave → OK.

## 7. Variables de entorno

Mecanismo: archivo `.env` en la raíz (ignorado por git). En local: `spring.config.import=optional:file:.env[.properties]`. En producción: `docker run --env-file .env` desde `dockerdeploy.ps1`. En `application.properties` cada una se referencia como `${VARIABLE:}`. **Nunca escribas valores en el repositorio ni en este documento.**

| Variable | Uso | Secreta |
|---|---|---|
| `BITRIX_WEBHOOK_URL` | Webhook entrante de Bitrix24 (scopes `crm`, `biconnector`…). Propiedad `bitrix.webhook-url`. | Sí |
| `B24_APP_PUBLIC_BASE_URL` | `https://erp.proelectricos.com/api` (base pública vía nginx). | No |
| `B24_CONNECTOR_KEY` | **Nueva.** Clave compartida que Bitrix envía al conector. Generar aleatoria (≥32 bytes). | Sí |
| `B24_CONNECTOR_DEFAULT_WINDOW_DAYS` | **Nueva.** Ventana por defecto del endpoint `data` (90). | No |
| `LOCAL_SOURCE_URL`, `LOCAL_SOURCE_TOKEN_URL`, `LOCAL_SOURCE_CLIENT_ID`, `LOCAL_SOURCE_CLIENT_SECRET` | Solo si el conector consumiera el endpoint por HTTP (no es el diseño elegido). | `CLIENT_SECRET` sí |

No aplican: `B24_PORTAL_URL` (incluida en el webhook), `B24_CLIENT_ID/SECRET` y `B24_APPLICATION_TOKEN` (no hay app OAuth), `B24_SMART_PROCESS_ENTITY_TYPE_ID` (no hay Smart Process), `SYNC_CRON`/`SYNC_BATCH_SIZE` (no hay sincronización), `B24_RATE_LIMIT_RPS` (solo se llama a la API REST para registrar el conector).

El `.env` también contiene variables del chatbot (Gemini, Telegram, TTS, usuario SQL de solo lectura). **No las toques** en esta tarea.

## 8. Riesgos y pendientes para el usuario

1. **Credenciales expuestas.** El documento de origen (`ClaudeBitrixSpring.docx`) contenía en texto plano: API key de Gemini, dos tokens de bot de Telegram, contraseña del usuario SQL `claude_reader`, token del webhook de Bitrix24 y `client_secret` del cliente Keycloak `Bitrix`. Además `application.properties` tenía contraseñas SQL y el token de Bitrix versionados. **Hay que rotarlas todas.**
2. **Datos personales.** Se respondió "no", pero el payload incluye NIT/cédula y nombre de clientes y vendedores (personas naturales en algunos casos). Confirmar que es aceptable enviarlos a Bitrix24 (Ley 1581 de 2012).
3. **Cliente Keycloak `Bitrix` sin restricción de rol**: puede leer casi toda la API. Crear un rol `bitrix` y proteger con él solo lo necesario (o desactivar el cliente si el conector no lo usa).
4. **Plan Professional**: confirmar en la página del plan que BI Builder y los conectores están incluidos.
5. **`FEC_MODI`/`FEC_MODIDE`** en `EMP001_FACT.dbo.PEDIDOS`: sin verificar su fiabilidad. No bloquea el conector (se usa ventana móvil).
6. **Trigger `trg_PEDIDOS_AfterUpdate_OrderTables`**: no se sabe si regenera los `orderReferId`. Por eso la clave estable de línea es `numero_pedido + posicion`, no `id_linea`.
7. **Salida HTTPS y nginx**: no comprobados desde dentro del servidor. Comandos de solo lectura para el usuario:
   ```
   docker --context linux-mds-server ps
   docker --context linux-mds-server exec mdsback-container env
   ```

## Anexo A – Muestra JSON (anonimizada)

```json
[
  {
    "orderHeaderId": 102339,
    "orderHeaderNumber": 49745,
    "orderHeaderDate": "2026-10-19",
    "orderHeaderPaymeConditions": "CREDITO  VS",
    "orderHeaderDescription": "OBRA EJEMPLO - TORRE 1",
    "orderHeaderBranch": {
      "branchId": 982,
      "branchCode": "01",
      "branchCity": "SOACHA (CUNDINAMARCA",
      "branchAddress": "CL 00 # 00-00 (anonimizada)",
      "branchClient": {
        "clientId": 2836,
        "clientThirdParty": {
          "thirdPartyId": 4673,
          "thirdPartyIdentNumber": 900000000,
          "thirdPartyName": "CLIENTE EJEMPLO S.A.S."
        }
      }
    },
    "orderHeaderVendor": {
      "vendorId": 184,
      "vendorThirdParty": {
        "thirdPartyId": 7641,
        "thirdPartyIdentNumber": 10000000,
        "thirdPartyName": "VENDEDOR EJEMPLO"
      },
      "vendorCode": "27197"
    },
    "orderHeaderBusinessLine": [
      { "referClassId": 1154, "referClassName": "celdas y tableros" }
    ],
    "orderReference": [
      {
        "orderReferId": 239499,
        "orderReferPosition": 1,
        "orderReferReference": {
          "referId": 164973,
          "referCod": "32114974501",
          "referCod2": null,
          "referName": "TABLERO PALNTA ELECTRICA \n UN 208/120V - IN 2000A - ICC 35KA",
          "referDescription": "TADI.T.2000A.65KA.IP43.IN.CR",
          "referMeasuUnit": { "measuUnitId": 4, "measuUnitName": "CONJUNTO" }
        },
        "orderReferBusinessLine": { "referClassId": 1154, "referClassName": "celdas y tableros" },
        "orderReferQuantity": 1,
        "orderReferUnitPrice": 17661953.0000,
        "valorTotal": 17661953.0000,
        "orderReferDelivDate": "2026-11-23",
        "orderReferProject": { "projeId": 14079, "projeName": "PROYECTO EJEMPLO" },
        "orderReferStatus": { "orderReferStatusId": 2, "orderReferStatusName": "Aprobado" }
      }
    ],
    "orderNote": [
      { "orderNoteId": 59857, "orderNotePosition": 1, "orderNoteText": "NOTA DE EJEMPLO 1" }
    ]
  }
]
```
