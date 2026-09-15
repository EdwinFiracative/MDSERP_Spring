package com.proelectricos.mdserp.model.entity.sqlfactory.emp001_inv;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import org.hibernate.annotations.Immutable;
import org.hibernate.annotations.Nationalized;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;

@Getter
@Entity
@Immutable
@Table(name = "vw_Pedidos_Clasificados", catalog = "EMP001_INV", schema = "dbo")
public class VwPedidosClasificado {

    @Id
    @Column(name = "id")
    private Long id;

    @Size(max = 7)
    @Column(name = "Numero_Pedido", length = 7)
    private String numeroPedido;

    @Size(max = 120)
    @Column(name = "Nombre_Cliente", length = 120)
    private String nombreCliente;

    @Size(max = 30)
    @Column(name = "Nombre_Vendedor", length = 30)
    private String nombreVendedor;

    @Column(name = "Fecha_Pedido")
    private LocalDate fechaPedido;

    @Size(max = 20)
    @Column(name = "Codigo_Producto", length = 20)
    private String codigoProducto;

    @Size(max = 200)
    @Column(name = "Nombre_Producto", length = 200)
    private String nombreProducto;

    @Size(max = 2)
    @Column(name = "Unidad_Medida", length = 2)
    private String unidadMedida;

    @Column(name = "Cantidad", precision = 14, scale = 4)
    private BigDecimal cantidad;

    @Column(name = "Costo_Unitario", precision = 16, scale = 4)
    private BigDecimal costoUnitario;

    @Column(name = "Costo_Estandar", precision = 16, scale = 4)
    private BigDecimal costoEstandar;

    @NotNull
    @Column(name = "Margen_Porcentaje", nullable = false, precision = 38, scale = 21)
    private BigDecimal margenPorcentaje;

    @Size(max = 50)
    @NotNull
    @Nationalized
    @Column(name = "Clase", nullable = false, length = 50)
    private String clase;

    @Column(name = "Cantidad_Pendiente", precision = 14, scale = 4)
    private BigDecimal cantidadPendiente;

    @Column(name = "Estado_Pedido")
    private Character estadoPedido;


}