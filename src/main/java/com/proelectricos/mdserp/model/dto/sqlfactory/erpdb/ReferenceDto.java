package com.proelectricos.mdserp.model.dto.sqlfactory.erpdb;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * DTO for {@link com.proelectricos.mdserp.model.entity.sqlfactory.erpdb.Reference}
 */
@Getter
@Setter
public class ReferenceDto implements Serializable {
    Long id;
    String referCod;
    String referCod2;
    String referName;
    String referDescription;
    MeasurUnitDto referMeasuUnit;
}
