package com.proelectricos.mdserp.model.dto.sqlfactory.emp001_comp;

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

@JsonPropertyOrder({ "COD","NOM","UD", "TIPO_OC", "FAPER", "OC", "vca_rco_proveedor", "FPROM", "OP", "pendiente", "ESTADO", "CANTP", "CANTE", "COSTO ESTANDAR", "cost_unitario", "venctto", "cos_pedido", "cos_entregado", "cos_pendiente", "AUTOFIN", "USR", "FAPROFIN", "CA_CLA_NI1_NOMBRE", "CA_CLA_NI2_NOMBRE", "CA_CLA_NI3_NOMBRE", "CA_CLA_NI4_NOMBRE", "CA_CLA_NI5_NOMBRE"
})
public class ViOrdAbiertasDto implements Serializable {

    /*private Long id;*/


    @JsonProperty("TIPO_OC")
    @Size(max = 1)
    @NotNull
    private String tipoOc;

    @JsonProperty("FAPER")
    private LocalDate faper;

    @JsonProperty("OC")
    private Integer oc;

    @JsonProperty("vca_rco_proveedor")
    @Size(max = 60)
    private String vcaRcoProveedor;

    @JsonProperty("COD")
    @Size(max = 20)
    private String cod;

    @JsonProperty("NOM")
    @Size(max = 100)
    private String nom;

    @JsonProperty("FPROM")
    private LocalDate fprom;

    @JsonProperty("OP")
    private String op;

    @JsonProperty("pendiente")
    private BigDecimal pendiente;

    @JsonProperty("ESTADO")
    private Character estado;

    @JsonProperty("CANTP")
    private BigDecimal cantp;

    @JsonProperty("CANTE")
    private BigDecimal cante;

    @JsonProperty("COSTO ESTANDAR")
    private BigDecimal costoEstandar;

    @JsonProperty("cost_unitario")
    private BigDecimal costUnitario;

    @JsonProperty("venctto")
    private Integer venctto;

    @JsonProperty("cos_pedido")
    private BigDecimal cosPedido;

    @JsonProperty("cos_entregado")
    private BigDecimal cosEntregado;

    @JsonProperty("cos_pendiente")
    private BigDecimal cosPendiente;

    @JsonProperty("UD")
    @Size(max = 2)
    private String ud;

    @JsonProperty("AUTOFIN")
    @Size(max = 6)
    private String autofin;

    @JsonProperty("USR")
    @Size(max = 25)
    private String usr;

    @JsonProperty("FAPROFIN")
    private LocalDate faprofin;

    @JsonProperty("ca_cla-ni1_nombre")
    @Size(max = 30)
    private String caClaNi1Nombre;

    @JsonProperty("ca_cla-ni2_nombre")
    @Size(max = 50)
    private String caClaNi2Nombre;

    @JsonProperty("ca_cla-ni3_nombre")
    @Size(max = 80)
    private String caClaNi3Nombre;

    @JsonProperty("ca_cla-ni4_nombre")
    @Size(max = 50)
    private String caClaNi4Nombre;

    @JsonProperty("ca_cla-ni5_nombre")
    @Size(max = 60)
    private String caClaNi5Nombre;
}
