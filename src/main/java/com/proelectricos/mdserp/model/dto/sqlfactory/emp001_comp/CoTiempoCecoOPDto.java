package com.proelectricos.mdserp.model.dto.sqlfactory.emp001_comp;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
public class CoTiempoCecoOPDto implements Serializable {

    /*private Long id;*/

    @JsonProperty("OP")
    private Integer op;

    @JsonProperty("COD")
    @Size(max = 20)
    private String cod;

    @JsonProperty("NOM")
    @Size(max = 40)
    private String nom;

    @JsonProperty("Cableado control")
    private Integer cableadoControl;

    @JsonProperty("Clinchado")
    private Integer clinchado;

    @JsonProperty("Coordinacion de proyectos tableros")
    private Integer coordinacionDeProyectosTableros;

    @JsonProperty("Diseno electrico")
    private Integer disenoElectrico;

    @JsonProperty("Diseno mecanico cajas especiales")
    private Integer disenoMecanicoCajasEspeciales;

    @JsonProperty("Diseno mecanico tableros")
    private Integer disenoMecanicoTableros;

    @JsonProperty("Doblez cnc")
    private Integer doblezCnc;

    @JsonProperty("Doblez manual")
    private Integer doblezManual;

    @JsonProperty("Empaque conduit")
    private Integer empaqueConduit;

    @JsonProperty("Ensamble armarios")
    private Integer ensambleArmarios;

    @JsonProperty("Ensamble celdas")
    private Integer ensambleCeldas;

    @JsonProperty("Ensamble mecanico y empaque doblado")
    private Integer ensambleMecanicoYEmpaqueDoblado;

    @JsonProperty("Ensamble tableros")
    private Integer ensambleTableros;

    @JsonProperty("Ensamble tableros enctos")
    private Integer ensambleTablerosEnctos;

    @JsonProperty("Fabricacion cobre")
    private Integer fabricacionCobre;

    @JsonProperty("Fabricacion cobre y corte manual")
    private Integer fabricacionCobreYCorteManual;

    @JsonProperty("Liberacion")
    private Integer liberacion;

    @JsonProperty("Marquillado")
    private Integer marquillado;

    @JsonProperty("Marquillas")
    private Integer marquillas;

    @JsonProperty("Montaje cobre y equipos tableros")
    private Integer montajeCobreYEquiposTableros;

    @JsonProperty("Preensamble de armarios")
    private Integer preensambleDeArmarios;

    @JsonProperty("Programacion")
    private Integer programacion;

    @JsonProperty("Pulido")
    private Integer pulido;

    @JsonProperty("Punzonado")
    private Integer punzonado;

    @JsonProperty("Retec")
    private Integer retec;

    @JsonProperty("Soldadura de pernos")
    private Integer soldaduraDePernos;

    @JsonProperty("Soldadura de punto")
    private Integer soldaduraDePunto;

    @JsonProperty("Soldadura mig")
    private Integer soldaduraMig;

    @JsonProperty("Soporte")
    private Integer soporte;

    @JsonProperty("Troquelado")
    private Integer troquelado;

    @JsonProperty("Zincado")
    private Integer zincado;
}
