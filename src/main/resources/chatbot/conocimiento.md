Conocimiento del negocio (Proeléctricos):

Glosario:
- "Pedido" = OrderHeader; "referencia", "línea" o "ítem" = OrderReference; "producto" = Reference.
- "Cliente" = ThirdParty unido por Branch -> Client -> ThirdParty; una sede (Branch) es un punto de entrega del cliente.
- "Vendedor" del pedido = OrderHeader.orderHeaderVendor y, si es NULL, Branch.branchVendor.
- "Ventas" o "valor vendido" = SUM(orderReferQuantity * orderReferUnitPrice), sin impuestos.

Reglas de los datos:
- orderReferDelivDate = '1900-01-01' significa que la línea no tiene fecha de entrega: exclúyela de cálculos de atraso y muéstrala como "Sin fecha".
- Para ventas y valores se excluyen las líneas en estado Anulado o Cancelado, salvo que pidan lo contrario.
- Los pedidos "pendientes" o "abiertos" son los que tienen líneas en estado Aprobado o En Revisión.
- Para buscar clientes o productos por nombre usa LIKE N'%texto%' (sin distinguir mayúsculas).

Consultas de ejemplo (adáptalas a la pregunta):

Top clientes por ventas de los últimos 12 meses:
SELECT TOP 20 tc.thirdPartyName AS Cliente, COUNT(DISTINCT h.orderHeaderId) AS Pedidos,
       SUM(r.orderReferQuantity * r.orderReferUnitPrice) AS ValorSinImpuestos
FROM dbo.OrderHeader h
JOIN dbo.Branch b ON b.branchId = h.orderHeaderBranch
JOIN dbo.Client c ON c.clientId = b.branchClient
JOIN dbo.ThirdParty tc ON tc.thirdPartyId = c.clientThirdParty
JOIN dbo.OrderReference r ON r.orderReferOrderHeader = h.orderHeaderId
JOIN dbo.OrderReferStatus s ON s.orderReferStatusId = r.orderReferStatus
WHERE h.orderHeaderDate >= DATEADD(MONTH, -12, CAST(GETDATE() AS date))
  AND s.orderReferStatusName NOT IN (N'Anulado', N'Cancelado')
GROUP BY tc.thirdPartyName
ORDER BY ValorSinImpuestos DESC;

Pedidos y ventas por mes del año actual:
SELECT TOP 20 YEAR(h.orderHeaderDate) AS Anio, MONTH(h.orderHeaderDate) AS Mes,
       COUNT(DISTINCT h.orderHeaderId) AS Pedidos, SUM(r.orderReferQuantity * r.orderReferUnitPrice) AS Valor
FROM dbo.OrderHeader h
JOIN dbo.OrderReference r ON r.orderReferOrderHeader = h.orderHeaderId
JOIN dbo.OrderReferStatus s ON s.orderReferStatusId = r.orderReferStatus
WHERE h.orderHeaderDate >= DATEFROMPARTS(YEAR(GETDATE()), 1, 1)
  AND s.orderReferStatusName NOT IN (N'Anulado', N'Cancelado')
GROUP BY YEAR(h.orderHeaderDate), MONTH(h.orderHeaderDate)
ORDER BY Anio, Mes;

Líneas pendientes con fecha de entrega vencida:
SELECT TOP 20 h.orderHeaderNumber AS Pedido, r.orderReferPosition AS Item, ref.referCod AS Codigo,
       ref.referName AS Nombre, r.orderReferDelivDate AS FechaEntrega, s.orderReferStatusName AS Estado
FROM dbo.OrderReference r
JOIN dbo.OrderHeader h ON h.orderHeaderId = r.orderReferOrderHeader
JOIN dbo.Reference ref ON ref.referId = r.orderReferReference
JOIN dbo.OrderReferStatus s ON s.orderReferStatusId = r.orderReferStatus
WHERE s.orderReferStatusName IN (N'Aprobado', N'En Revisión')
  AND r.orderReferDelivDate > '1900-01-01' AND r.orderReferDelivDate < CAST(GETDATE() AS date)
ORDER BY r.orderReferDelivDate;
