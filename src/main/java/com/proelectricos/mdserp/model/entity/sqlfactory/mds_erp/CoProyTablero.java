package com.proelectricos.mdserp.model.entity.sqlfactory.mds_erp;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Immutable;
import org.hibernate.annotations.Nationalized;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "co_proy_tableros", catalog = "MDS_ERP", schema = "dbo")
@Immutable

public class CoProyTablero {
    @Id
    @Column(name = "id")
    private Long id;

    @Size(max = 7)
    @Column(name = "PEDIDO", length = 7)
    private String pedido;

    @Column(name = "OP")
    private Integer op;

    @Size(max = 20)
    @Column(name = "MASTER", length = 20)
    private String master;

    @Column(name = "\"FECHA PEDIDO\"")
    private LocalDate fechaPedido;

    @Column(name = "\"FECHA FINAL\"")
    private LocalDate fechaFinal;

    @Size(max = 50)
    @Column(name = "\"FECHA REAL ENTREGA\"", length = 50)
    private String fechaRealEntrega;

    @Size(max = 6)
    @Column(name = "PM", length = 6)
    private String pm;

    @Size(max = 30)
    @Column(name = "VENDEDOR", length = 30)
    private String vendedor;

    @Size(max = 120)
    @Column(name = "CLIENTE", length = 120)
    private String cliente;

    @Size(max = 250)
    @Column(name = "PROYECTO", length = 250)
    private String proyecto;

    @Size(max = 200)
    @Column(name = "\"DESCRIPCIÓN\"", length = 200)
    private String descripción;

    @Size(max = 40)
    @Column(name = "\"DESCRIPCIÓN2\"", length = 40)
    private String descripción2;

    @Size(max = 100)
    @Column(name = "REFERENCIA", length = 100)
    private String referencia;

    @Column(name = "CANT_PED", precision = 14, scale = 4)
    private BigDecimal cantPed;

    @Column(name = "ESTADO")
    private Character estado;

    @Column(name = "MODELO")
    private Character modelo;

    @Column(name = "CANTP", precision = 14, scale = 4)
    private BigDecimal cantp;

    @Column(name = "CANTE", precision = 14, scale = 4)
    private BigDecimal cante;

    @Column(name = "CAN_SALDO", precision = 15, scale = 4)
    private BigDecimal canSaldo;

    @Column(name = "FECHA_T")
    private LocalDate fechaT;

    @Column(name = "\"DISP ELEC MEC PLAN\"")
    private LocalDate dispElecMecPlan;

    @Column(name = "\"DISP ELEC MEC REAL\"")
    private LocalDate dispElecMecReal;

    @Column(name = "\"APROBACIÓN PLAN\"")
    private LocalDate aprobaciNPlan;

    @Column(name = "\"APROBACIÓN REAL\"")
    private LocalDate aprobaciNReal;

    @Column(name = "\"COMPROMISO Y ENTREGA PLAN\"")
    private LocalDate compromisoYEntregaPlan;

    @Column(name = "\"COMPROMISO Y ENTREGA REAL\"")
    private LocalDate compromisoYEntregaReal;

    @Column(name = "\"LMF Y CONSUMIBLES PLAN\"")
    private LocalDate lmfYConsumiblesPlan;

    @Column(name = "\"LMF Y CONSUMIBLES REAL\"")
    private LocalDate lmfYConsumiblesReal;

    @Column(name = "\"COMPRAS PLAN\"")
    private LocalDate comprasPlan;

    @Column(name = "\"COMPRAS REAL\"")
    private LocalDate comprasReal;

    @Column(name = "\"DISEÑO MECÁNICO PLAN\"")
    private LocalDate diseOMecNicoPlan;

    @Column(name = "\"DISEÑO MECÁNICO REAL\"")
    private LocalDate diseOMecNicoReal;

    @Column(name = "\"METALMECÁNICA PLAN\"")
    private LocalDate metalmecNicaPlan;

    @Column(name = "\"METALMECÁNICA REAL\"")
    private LocalDate metalmecNicaReal;

    @Column(name = "\"MATERIAL ELÉCTRICO PLAN\"")
    private LocalDate materialElCtricoPlan;

    @Column(name = "\"MATERIAL ELÉCTRICO REAL\"")
    private LocalDate materialElCtricoReal;

    @Column(name = "\"DESPACHO PLAN\"")
    private LocalDate despachoPlan;

    @Column(name = "\"DESPACHO REAL\"")
    private LocalDate despachoReal;

    @Column(name = "\"COSTO TOTAL FABRICACIÓN\"", precision = 36, scale = 15)
    private BigDecimal costoTotalFabricaciN;

    @Column(name = "\"COSTO TOTAL OFERTA\"", precision = 31, scale = 8)
    private BigDecimal costoTotalOferta;

    @Column(name = "PREC_VENTA", precision = 16, scale = 4)
    private BigDecimal precVenta;

    @Column(name = "PREC_SALDO", precision = 32, scale = 8)
    private BigDecimal precSaldo;

    @Column(name = "\"PRECIO VENTA TOTAL\"", precision = 31, scale = 8)
    private BigDecimal precioVentaTotal;

    @Column(name = "MAR_ESPE", precision = 38, scale = 21)
    private BigDecimal marEspe;

    @Column(name = "MAR_REAL", precision = 38, scale = 12)
    private BigDecimal marReal;

    @Size(max = 50)
    @NotNull
    @Nationalized
    @Column(name = "\"ca_cla-ni2_nombre\"", nullable = false, length = 50)
    private String caClaNi2Nombre;

    @Size(max = 80)
    @NotNull
    @Nationalized
    @Column(name = "\"ca_cla-ni3_nombre\"", nullable = false, length = 80)
    private String caClaNi3Nombre;

    @Size(max = 50)
    @NotNull
    @Nationalized
    @Column(name = "\"ca_cla-ni4_nombre\"", nullable = false, length = 50)
    private String caClaNi4Nombre;

    @Size(max = 60)
    @NotNull
    @Nationalized
    @Column(name = "\"ca_cla-ni5_nombre\"", nullable = false, length = 60)
    private String caClaNi5Nombre;

    @Column(name = "FECHA_I")
    private LocalDate fechaI;

    @Size(max = 12)
    @Column(name = "ENTRADA", length = 12)
    private String entrada;

    @Size(max = 12)
    @Column(name = "SALIDA", length = 12)
    private String salida;

    @Size(max = 12)
    @Column(name = "\"AJUSTE INV\"", length = 12)
    private String ajusteInv;

    @Size(max = 12)
    @Column(name = "\"DEVOLUCIÓN\"", length = 12)
    private String devolución;

    @Size(max = 7)
    @Column(name = "ESTADO_OP", length = 7)
    private String estadoOp;

    @Column(name = "\"CANT PINTURA ST\"", precision = 38, scale = 11)
    private BigDecimal cantPinturaSt;

    @Size(max = 40)
    @Column(name = "OBS0", length = 40)
    private String obs0;

    @Size(max = 40)
    @Column(name = "OBS1", length = 40)
    private String obs1;

    @Size(max = 40)
    @Column(name = "OBS2", length = 40)
    private String obs2;

    @Size(max = 40)
    @Column(name = "OBS3", length = 40)
    private String obs3;

    @Size(max = 40)
    @Column(name = "OBS4", length = 40)
    private String obs4;

    @Size(max = 40)
    @Column(name = "OBS5", length = 40)
    private String obs5;

    @Size(max = 40)
    @Column(name = "OBS6", length = 40)
    private String obs6;

    @Size(max = 40)
    @Column(name = "OBS7", length = 40)
    private String obs7;

    @Size(max = 40)
    @Column(name = "OBS8", length = 40)
    private String obs8;

    @Column(name = "\"SEMANA DE FINALIZACION\"")
    private Integer semanaDeFinalizacion;

    @Column(name = "\"MES DE FINALIZACION\"")
    private Integer mesDeFinalizacion;

    @Column(name = "\"AÑO\"")
    private Integer año;

    @Nationalized
    @Lob
    @Column(name = "\"REM:CANT\"")
    private String remCant;


}
