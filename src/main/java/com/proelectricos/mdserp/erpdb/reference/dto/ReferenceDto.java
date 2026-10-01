package com.proelectricos.mdserp.erpdb.reference.dto;

import com.proelectricos.mdserp.erpdb.measurunit.dto.MeasurUnitDto;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * DTO for {@link com.proelectricos.mdserp.erpdb.reference.Reference}
 */
@Getter
@Setter
public class ReferenceDto implements Serializable {
    Long referId;
    String referCod;
    String referCod2;
    String referName;
    String referDescription;
    MeasurUnitDto referMeasuUnit;
}
