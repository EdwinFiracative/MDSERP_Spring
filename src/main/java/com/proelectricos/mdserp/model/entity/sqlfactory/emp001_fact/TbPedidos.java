package com.proelectricos.mdserp.model.entity.sqlfactory.emp001_fact;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
@Entity
@Table(name = "PEDIDOS", catalog = "EMP001_FACT", schema = "dbo")
public class TbPedidos {

    @Id
    @NotNull
    @Column(name = "Id", nullable = false)
    private Integer id;

    @Size(max = 5)
    @Column(name = "POSICION", length = 5)
    private String posicion;

    @Size(max = 15)
    @Column(name = "DESTINAT", length = 15)
    private String destinat;

    @Size(max = 250)
    @Column(name = "TDESPACHO", length = 250)
    private String tdespacho;

    @Size(max = 7)
    @Column(name = "NUM", length = 7)
    private String num;

    @Size(max = 15)
    @Column(name = "CLIENTE", length = 15)
    private String cliente;

    @Column(name = "FECHA")
    private LocalDate fecha;

    @Size(max = 3)
    @Column(name = "SECCION", length = 3)
    private String seccion;

    @Size(max = 20)
    @Column(name = "COD", length = 20)
    private String cod;

    @Size(max = 200)
    @Column(name = "NOM", length = 200)
    private String nom;

    @Size(max = 2)
    @Column(name = "UD", length = 2)
    private String ud;

    @Column(name = "CANT", precision = 14, scale = 4)
    private BigDecimal cant;

    @Column(name = "COSTO", precision = 16, scale = 4)
    private BigDecimal costo;

    @Column(name = "PEND", precision = 14, scale = 4)
    private BigDecimal pend;

    @Column(name = "ESTADO")
    private Character estado;

    @Column(name = "DESP", precision = 14, scale = 4)
    private BigDecimal desp;

    @Column(name = "DESC1", precision = 8, scale = 4)
    private BigDecimal desc1;

    @Column(name = "DESC2", precision = 8, scale = 4)
    private BigDecimal desc2;

    @Column(name = "DESC3", precision = 8, scale = 4)
    private BigDecimal desc3;

    @Size(max = 4)
    @Column(name = "ZONA", length = 4)
    private String zona;

    @Column(name = "NETO", precision = 16, scale = 4)
    private BigDecimal neto;

    @Column(name = "DIAS")
    private Integer dias;

    @Size(max = 5)
    @Column(name = "VENDEDOR", length = 5)
    private String vendedor;

    @Size(max = 3)
    @Column(name = "LIN", length = 3)
    private String lin;

    @Column(name = "ENTREGA")
    private LocalDate entrega;

    @Size(max = 50)
    @Column(name = "ORDEN", length = 50)
    private String orden;

    @Size(max = 6)
    @Column(name = "NCOD", length = 6)
    private String ncod;

    @Column(name = "DET")
    private Integer det;

    @Column(name = "PENDP")
    private Integer pendp;

    @Column(name = "FECHAN")
    private LocalDate fechan;

    @Column(name = "FECHAP")
    private LocalDate fechap;

    @Size(max = 4)
    @Column(name = "TIPO", length = 4)
    private String tipo;

    @Column(name = "FALTA", precision = 14, scale = 4)
    private BigDecimal falta;

    @Size(max = 25)
    @Column(name = "ATN", length = 25)
    private String atn;

    @Column(name = "ANTI", precision = 16, scale = 4)
    private BigDecimal anti;

    @Column(name = "COND1")
    private Integer cond1;

    @Column(name = "COND2")
    private Integer cond2;

    @Column(name = "COND3")
    private Integer cond3;

    @Size(max = 10)
    @Column(name = "SYS2015", length = 10)
    private String sys2015;

    @Lob
    @Column(name = "DETALLE")
    private String detalle;

    @Size(max = 10)
    @Column(name = "HORAS", length = 10)
    private String horas;

    @Size(max = 2)
    @Column(name = "URGENTE", length = 2)
    private String urgente;

    @Size(max = 10)
    @Column(name = "COND_CTE", length = 10)
    private String condCte;

    @Size(max = 10)
    @Column(name = "COND_PED", length = 10)
    private String condPed;

    @Size(max = 2)
    @Column(name = "DOCUMENTOS", length = 2)
    private String documentos;

    @Size(max = 50)
    @Column(name = "DESPACHO", length = 50)
    private String despacho;

    @Column(name = "EXACTA")
    private Character exacta;

    @Size(max = 250)
    @Column(name = "OBSERV", length = 250)
    private String observ;

    @Size(max = 15)
    @Column(name = "RESPON", length = 15)
    private String respon;

    @Column(name = "PIEZAS")
    private Integer piezas;

    @Size(max = 50)
    @Column(name = "PROCESO", length = 50)
    private String proceso;

    @Size(max = 10)
    @Column(name = "DUREZAP", length = 10)
    private String durezap;

    @Size(max = 10)
    @Column(name = "DUREZAR", length = 10)
    private String durezar;

    @Column(name = "TIPOMANEJO")
    private Character tipomanejo;

    @Column(name = "MANEJO")
    private Character manejo;

    @Size(max = 10)
    @Column(name = "PLANO", length = 10)
    private String plano;

    @Column(name = "SELLO")
    private Character sello;

    @Column(name = "KILOS", precision = 10, scale = 4)
    private BigDecimal kilos;

    @Column(name = "SECUENCIA", precision = 6)
    private BigDecimal secuencia;

    @Size(max = 12)
    @Column(name = "LOTE", length = 12)
    private String lote;

    @Column(name = "TRMUSD", precision = 10, scale = 4)
    private BigDecimal trmusd;

    @Size(max = 10)
    @Column(name = "CONTENIDO", length = 10)
    private String contenido;

    @Size(max = 40)
    @Column(name = "M_EMPLEO", length = 40)
    private String mEmpleo;

    @Column(name = "REG_NUM", precision = 7)
    private BigDecimal regNum;

    @Column(name = "FVENCE")
    private LocalDate fvence;

    @Column(name = "CANT1", precision = 14, scale = 4)
    private BigDecimal cant1;

    @Size(max = 60)
    @Column(name = "COD_DANE", length = 60)
    private String codDane;

    @Size(max = 10)
    @Column(name = "PLACA", length = 10)
    private String placa;

    @Column(name = "VALORM", precision = 16, scale = 2)
    private BigDecimal valorm;

    @Size(max = 30)
    @Column(name = "BARRIO", length = 30)
    private String barrio;

    @Column(name = "DESP1", precision = 10, scale = 2)
    private BigDecimal desp1;

    @Size(max = 10)
    @Column(name = "ALMA", length = 10)
    private String alma;

    @Size(max = 10)
    @Column(name = "DUREZA", length = 10)
    private String dureza;

    @Column(name = "PEND1", precision = 14, scale = 4)
    private BigDecimal pend1;

    @Column(name = "RESERVA", precision = 16, scale = 4)
    private BigDecimal reserva;

    @Column(name = "RENTREGA", precision = 16, scale = 4)
    private BigDecimal rentrega;

    @Column(name = "PROMEDIO", precision = 14, scale = 4)
    private BigDecimal promedio;

    @Size(max = 15)
    @Column(name = "CFINAL", length = 15)
    private String cfinal;

    @Size(max = 200)
    @Column(name = "NOMADJUNTO", length = 200)
    private String nomadjunto;

    @Size(max = 200)
    @Column(name = "RUTAARCH", length = 200)
    private String rutaarch;

    @Size(max = 2)
    @Column(name = "LINEA", length = 2)
    private String linea;

    @Size(max = 10)
    @Column(name = "CENTRO", length = 10)
    private String centro;

    @Lob
    @Column(name = "OBSER_RENG")
    private String obserReng;

    @Size(max = 10)
    @Column(name = "MARCA", length = 10)
    private String marca;

    @Size(max = 4)
    @Column(name = "PREFIJOFAC", length = 4)
    private String prefijofac;

    @Size(max = 30)
    @Column(name = "CONDIC", length = 30)
    private String condic;



    @Column(name = "DIM_1", precision = 16, scale = 4)
    private BigDecimal dim1;

    @Column(name = "DIM_2", precision = 16, scale = 4)
    private BigDecimal dim2;

    @Size(max = 5)
    @Column(name = "CODCIUVEN", length = 5)
    private String codciuven;

    @Size(max = 5)
    @Column(name = "CODCIUSERV", length = 5)
    private String codciuserv;

    @Size(max = 5)
    @Column(name = "CODCIUTRAN", length = 5)
    private String codciutran;

    @Size(max = 10)
    @Column(name = "USU_CREA", length = 10)
    private String usuCrea;

    @Column(name = "FEC_CREA")
    private LocalDate fecCrea;

    @Column(name = "FEC_MODI")
    private LocalDate fecModi;

    @Size(max = 4)
    @Column(name = "TIPOPED", length = 4)
    private String tipoped;

    @Size(max = 10)
    @Column(name = "USU_MODIDE", length = 10)
    private String usuModide;

    @Column(name = "FEC_MODIDE")
    private LocalDate fecModide;

    @Column(name = "FENTREGA_O")
    private LocalDate fentregaO;

    @Column(name = "FECHAAPROB")
    private LocalDate fechaaprob;

    @Size(max = 7)
    @Column(name = "NUM_COM", length = 7)
    private String numCom;

    @Column(name = "FECHA_COM")
    private LocalDate fechaCom;

    @Column(name = "COM_TIPO")
    private Character comTipo;

    @Size(max = 10)
    @Column(name = "USUA_COM", length = 10)
    private String usuaCom;

    @Column(name = "LATITUD", precision = 20, scale = 12)
    private BigDecimal latitud;

    @Column(name = "LONGITUD", precision = 20, scale = 12)
    private BigDecimal longitud;

    @Size(max = 30)
    @Column(name = "ETIQUETDIR", length = 30)
    private String etiquetdir;

    @Column(name = "APROBCAR")
    private Character aprobcar;

    @Column(name = "APROBFIN")
    private Character aprobfin;

    @Column(name = "FAPROBCAR")
    private LocalDate faprobcar;

    @Column(name = "FAPROBFIN")
    private LocalDate faprobfin;

    @Size(max = 6)
    @Column(name = "USUACAR", length = 6)
    private String usuacar;

    @Size(max = 6)
    @Column(name = "USUAFIN", length = 6)
    private String usuafin;

    @Size(max = 80)
    @Column(name = "APROBMOTV", length = 80)
    private String aprobmotv;

    @Size(max = 200)
    @Column(name = "RECHAZAMOT", length = 200)
    private String rechazamot;

    @Column(name = "LISTAPREC")
    private Character listaprec;

    @Size(max = 200)
    @Column(name = "ADJUNTOOC", length = 200)
    private String adjuntooc;

    @Column(name = "PORCIMPCON", precision = 10, scale = 4)
    private BigDecimal porcimpcon;

    @Column(name = "FHORAENT")
    private LocalDate fhoraent;

    @Column(name = "FABRICA", precision = 1)
    private BigDecimal fabrica;

    @Column(name = "CANTFAB", precision = 18, scale = 4)
    private BigDecimal cantfab;

    @Size(max = 10)
    @Column(name = "ESTADO2", length = 10)
    private String estado2;

    @Column(name = "FECHAR")
    private LocalDate fechar;

    @Size(max = 20)
    @Column(name = "TIPO2", length = 20)
    private String tipo2;

    @Size(max = 10)
    @Column(name = "NOCOBRA", length = 10)
    private String nocobra;

    @Column(name = "FECHAIC")
    private LocalDate fechaic;

    @Column(name = "FECHAORDEN")
    private LocalDate fechaorden;

    @Size(max = 15)
    @Column(name = "CODCLOCAL", length = 15)
    private String codclocal;

    @Size(max = 15)
    @Column(name = "CODCPENT", length = 15)
    private String codcpent;

    @Column(name = "PORCIMPULT", precision = 10, scale = 2)
    private BigDecimal porcimpult;

    @Column(name = "VLRTARIBEB", precision = 18, scale = 4)
    private BigDecimal vlrtaribeb;

    @NotNull
    @ColumnDefault("0")
    @Column(name = "ASUMIDO", nullable = false, precision = 10, scale = 2)
    private BigDecimal asumido;

    @Column(name = "VLRTARINPP", precision = 18, scale = 4)
    private BigDecimal vlrtarinpp;

    @Column(name = "VLRIMPINPP", precision = 18, scale = 4)
    private BigDecimal vlrimpinpp;

    @Column(name = "VAL")
    private Integer val;

    @Column(name = "PLANOCEN")
    private Integer planocen;

    @Size(max = 4)
    @Column(name = "ESTADO3", length = 4)
    private String estado3;

    @Column(name = "CATEGORIA")
    private Integer categoria;

    @Size(max = 100)
    @Column(name = "CORREORES", length = 100)
    private String correores;

    @Lob
    @Column(name = "COMENMAIL")
    private String comenmail;

    @Column(name = "FECAPROB")
    private LocalDate fecaprob;


}