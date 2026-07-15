package com.proelectricos.mdserp.model.dto.sqlfactory.emp001_fact;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
@JsonPropertyOrder({
        "PREFIJO", "NUMERO", "FECFACT", "CLIENTE", "NIT", "NOM", "PERIODO",
        "VLR_FRA", "ESTADO", "NUM_FACT_PAGOS", "CONC", "RECIBO", "FECREC", "VALOR",
        "BANCO", "ENTIDAD", "DETA", "VENDEDOR", "EJECUTIVO", "VALORX", "COSTO ESTANDAR",
        "MARGEN", "CIU", "VR_MCIA", "CONCEPTO", "FDCTO", "DIAS_PAGO_FEC_FACTURA",
        "DIAS_PAGO_FEC_VCTO", "AÑO_RECIBO", "PERIODO_RECIBO", "PEDIDO", "NUM",
        "LINEAS", "CUMPLIMIENTO PAGOS"
})
public class CoPagosCarDto implements Serializable {

   /* @JsonProperty("id")
    private Long id;*/

    @JsonProperty("PREFIJO")
    @Size(max = 4)
    private String PREFIJO;

    @JsonProperty("NUMERO")
    private Integer NUMERO;

    @JsonProperty("FECFACT")
    private LocalDate FECFACT;

    @JsonProperty("CLIENTE")
    @Size(max = 15)
    private String CLIENTE;

    @JsonProperty("NIT")
    @Size(max = 15)
    private String NIT;

    @JsonProperty("NOM")
    @Size(max = 120)
    private String NOM;

    @JsonProperty("PERIODO")
    @Size(max = 7)
    private String PERIODO;

    @JsonProperty("VLR_FRA")
    private BigDecimal VLR_FRA;

    @JsonProperty("ESTADO")
    private Character ESTADO;

    @JsonProperty("NUM_FACT_PAGOS")
    private Integer NUM_FACT_PAGOS;

    @JsonProperty("CONC")
    @Size(max = 4)
    private String CONC;

    @JsonProperty("RECIBO")
    @Size(max = 6)
    private String RECIBO;

    @JsonProperty("FECREC")
    private LocalDate FECREC;

    @JsonProperty("VALOR")
    private BigDecimal VALOR;

    @JsonProperty("BANCO")
    @Size(max = 2)
    private String BANCO;

    @JsonProperty("ENTIDAD")
    @Size(max = 20)
    private String ENTIDAD;

    @JsonProperty("DETA")
    @Size(max = 120)
    private String DETA;

    @JsonProperty("VENDEDOR")
    @Size(max = 5)
    private String VENDEDOR;

    @JsonProperty("EJECUTIVO")
    @Size(max = 30)
    private String EJECUTIVO;

    @JsonProperty("VALORX")
    private BigDecimal VALORX;

    @JsonProperty("COSTO ESTANDAR")
    private BigDecimal COSTO_ESTANDAR;

    @JsonProperty("MARGEN")
    private BigDecimal MARGEN;

    @JsonProperty("CIU")
    @Size(max = 25)
    private String CIU;

    @JsonProperty("VR_MCIA")
    private BigDecimal VR_MCIA;

    @JsonProperty("CONCEPTO")
    @Size(max = 50)
    private String CONCEPTO;

    @JsonProperty("FDCTO")
    private LocalDate FDCTO;

    @JsonProperty("DIAS_PAGO_FEC_FACTURA")
    private Integer DIAS_PAGO_FEC_FACTURA;

    @JsonProperty("DIAS_PAGO_FEC_VCTO")
    private Integer DIAS_PAGO_FEC_VCTO;

    @JsonProperty("AÑO_RECIBO")
    private Integer AÑO_RECIBO;

    @JsonProperty("PERIODO_RECIBO")
    @Size(max = 7)
    private String PERIODO_RECIBO;

    @JsonProperty("PEDIDO")
    @Size(max = 5)
    private String PEDIDO;

    @JsonProperty("NUM")
    @Size(max = 7)
    private String NUM;

    @JsonProperty("LINEAS")
    @Size(max = 50)
    private String LINEAS;

    @JsonProperty("CUMPLIMIENTO PAGOS")
    @NotNull
    @Size(max = 10)
    private String CUMPLIMIENTO_PAGOS;
}

