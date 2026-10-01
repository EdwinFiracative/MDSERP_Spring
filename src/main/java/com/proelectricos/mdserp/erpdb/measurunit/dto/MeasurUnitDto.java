package com.proelectricos.mdserp.erpdb.measurunit.dto;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * DTO for {@link com.proelectricos.mdserp.erpdb.measurunit.MeasurUnit}
 */
@Getter
@Setter
public class MeasurUnitDto implements Serializable {
    Long measuUnitId;
    String measuUnitName;
}
