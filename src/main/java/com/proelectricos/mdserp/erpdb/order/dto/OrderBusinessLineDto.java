package com.proelectricos.mdserp.erpdb.order.dto;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * Linea de negocio: nodo de segundo nivel del arbol de clasificacion (hijo directo de la raiz).
 * Construido desde {@link com.proelectricos.mdserp.erpdb.referclassification.ReferClassification}.
 */
@Getter
@Setter
public class OrderBusinessLineDto implements Serializable {
    Long referClassId;
    String referClassName;
}
