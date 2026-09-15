package com.proelectricos.mdserp.model.dto.sqlfactory.emp001_inv;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;

@Getter
@Setter
public class VwPedidosClasificadoDto implements Serializable {

   /* private Long id;*/

    @Size(max = 7)
    @JsonProperty("Numero_Pedido")
    private String numeroPedido;

    @Size(max = 120)
    @JsonProperty("Nombre_Cliente")
    private String nombreCliente;

    @Size(max = 30)
    @JsonProperty("Nombre_Vendedor")
    private String nombreVendedor;

    @JsonProperty("Fecha_Pedido")
    private LocalDate fechaPedido;

    @Size(max = 20)
    @JsonProperty("Codigo_Producto")
    private String codigoProducto;

    @Size(max = 200)
    @JsonProperty("Nombre_Producto")
    private String nombreProducto;

    @Size(max = 2)
    @JsonProperty("Unidad_Medida")
    private String unidadMedida;

    @JsonProperty("Cantidad")
    private BigDecimal cantidad;

    @JsonProperty("Costo_Unitario")
    private BigDecimal costoUnitario;

    @JsonProperty("Costo_Estandar")
    private BigDecimal costoEstandar;

    @NotNull
    @JsonProperty("Margen_Porcentaje")
    private BigDecimal margenPorcentaje;

    @Size(max = 50)
    @NotNull
    @JsonProperty("Clase")
    private String clase;

    @JsonProperty("Cantidad_Pendiente")
    private BigDecimal cantidadPendiente;

    @JsonProperty("Estado_Pedido")
    private Character estadoPedido;


}
