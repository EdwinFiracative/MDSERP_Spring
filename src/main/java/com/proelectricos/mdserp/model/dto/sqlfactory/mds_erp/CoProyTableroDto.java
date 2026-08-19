package com.proelectricos.mdserp.model.dto.sqlfactory.mds_erp;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import lombok.Value;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * DTO for {@link com.proelectricos.mdserp.model.entity.sqlfactory.mds_erp.CoProyTablero}
 */
@Getter
@Setter
public class CoProyTableroDto implements Serializable {
    //Long id;
    @Size(max = 7)
    String pedido;
    Integer op;
    @Size(max = 20)
    String master;
    LocalDate fechaPedido;
    LocalDate fechaFinal;
    @Size(max = 50)
    String fechaRealEntrega;
    @Size(max = 6)
    String pm;
    @Size(max = 30)
    String vendedor;
    @Size(max = 120)
    String cliente;
    @Size(max = 250)
    String proyecto;
    @Size(max = 200)
    String descripción;
    @Size(max = 40)
    String descripción2;
    @Size(max = 100)
    String referencia;
    BigDecimal cantPed;
    Character estado;
    Character modelo;
    BigDecimal cantp;
    BigDecimal cante;
    BigDecimal canSaldo;
    LocalDate fechaT;
    LocalDate dispElecMecPlan;
    LocalDate dispElecMecReal;
    LocalDate aprobaciNPlan;
    LocalDate aprobaciNReal;
    LocalDate compromisoYEntregaPlan;
    LocalDate compromisoYEntregaReal;
    LocalDate lmfYConsumiblesPlan;
    LocalDate lmfYConsumiblesReal;
    LocalDate comprasPlan;
    LocalDate comprasReal;
    LocalDate diseOMecNicoPlan;
    LocalDate diseOMecNicoReal;
    LocalDate metalmecNicaPlan;
    LocalDate metalmecNicaReal;
    LocalDate materialElCtricoPlan;
    LocalDate materialElCtricoReal;
    LocalDate despachoPlan;
    LocalDate despachoReal;
    BigDecimal costoTotalFabricaciN;
    BigDecimal costoTotalOferta;
    BigDecimal precVenta;
    BigDecimal precSaldo;
    BigDecimal precioVentaTotal;
    BigDecimal marEspe;
    BigDecimal marReal;
    @NotNull
    @Size(max = 50)
    String caClaNi2Nombre;
    @NotNull
    @Size(max = 80)
    String caClaNi3Nombre;
    @NotNull
    @Size(max = 50)
    String caClaNi4Nombre;
    @NotNull
    @Size(max = 60)
    String caClaNi5Nombre;
    LocalDate fechaI;
    @Size(max = 12)
    String entrada;
    @Size(max = 12)
    String salida;
    @Size(max = 12)
    String ajusteInv;
    @Size(max = 12)
    String devolución;
    @Size(max = 7)
    String estadoOp;
    BigDecimal cantPinturaSt;
    @Size(max = 40)
    String obs0;
    @Size(max = 40)
    String obs1;
    @Size(max = 40)
    String obs2;
    @Size(max = 40)
    String obs3;
    @Size(max = 40)
    String obs4;
    @Size(max = 40)
    String obs5;
    @Size(max = 40)
    String obs6;
    @Size(max = 40)
    String obs7;
    @Size(max = 40)
    String obs8;
    Integer semanaDeFinalizacion;
    Integer mesDeFinalizacion;
    Integer año;
    String remCant;
}

