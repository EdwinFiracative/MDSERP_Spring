package com.proelectricos.mdserp.model.entity.sqlfactory.emp001_inv;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import org.hibernate.annotations.Immutable;

@Getter
@Entity
@Immutable
@Table(name = "coTiempoCecoOP_2026", catalog = "EMP001_INV", schema = "dbo")
public class CoTiempoCecoOp {

    @Id
    @Column(name = "id")
    private Long id;

    @Column(name = "OP")
    private Integer op;

    @Size(max = 20)
    @Column(name = "COD", length = 20)
    private String cod;

    @Size(max = 40)
    @Column(name = "NOM", length = 40)
    private String nom;

    @Column(name = "\"Cableado control\"")
    private Integer cableadoControl;

    @Column(name = "Clinchado")
    private Integer clinchado;

    @Column(name = "\"Coordinacion de proyectos tableros\"")
    private Integer coordinacionDeProyectosTableros;

    @Column(name = "\"Diseno electrico\"")
    private Integer disenoElectrico;

    @Column(name = "\"Diseno mecanico cajas especiales\"")
    private Integer disenoMecanicoCajasEspeciales;

    @Column(name = "\"Diseno mecanico tableros\"")
    private Integer disenoMecanicoTableros;

    @Column(name = "\"Doblez cnc\"")
    private Integer doblezCnc;

    @Column(name = "\"Doblez manual\"")
    private Integer doblezManual;

    @Column(name = "\"Empaque conduit\"")
    private Integer empaqueConduit;

    @Column(name = "\"Ensamble armarios\"")
    private Integer ensambleArmarios;

    @Column(name = "\"Ensamble celdas\"")
    private Integer ensambleCeldas;

    @Column(name = "\"Ensamble mecanico y empaque doblado\"")
    private Integer ensambleMecanicoYEmpaqueDoblado;

    @Column(name = "\"Ensamble tableros\"")
    private Integer ensambleTableros;

    @Column(name = "\"Ensamble tableros enctos\"")
    private Integer ensambleTablerosEnctos;

    @Column(name = "\"Fabricacion cobre\"")
    private Integer fabricacionCobre;

    @Column(name = "\"Fabricacion cobre y corte manual\"")
    private Integer fabricacionCobreYCorteManual;

    @Column(name = "Liberacion")
    private Integer liberacion;

    @Column(name = "Marquillado")
    private Integer marquillado;

    @Column(name = "Marquillas")
    private Integer marquillas;

    @Column(name = "\"Montaje cobre y equipos tableros\"")
    private Integer montajeCobreYEquiposTableros;

    @Column(name = "\"Preensamble de armarios\"")
    private Integer preensambleDeArmarios;

    @Column(name = "Programacion")
    private Integer programacion;

    @Column(name = "Pulido")
    private Integer pulido;

    @Column(name = "Punzonado")
    private Integer punzonado;

    @Column(name = "Retec")
    private Integer retec;

    @Column(name = "\"Soldadura de pernos\"")
    private Integer soldaduraDePernos;

    @Column(name = "\"Soldadura de punto\"")
    private Integer soldaduraDePunto;

    @Column(name = "\"Soldadura mig\"")
    private Integer soldaduraMig;

    @Column(name = "Soporte")
    private Integer soporte;

    @Column(name = "Troquelado")
    private Integer troquelado;

    @Column(name = "Zincado")
    private Integer zincado;


}