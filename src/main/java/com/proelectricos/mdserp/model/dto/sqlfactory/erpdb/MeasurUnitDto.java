package com.proelectricos.mdserp.model.dto.sqlfactory.erpdb;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * DTO for {@link com.proelectricos.mdserp.model.entity.sqlfactory.erpdb.MeasurUnit}
 */
@Getter
@Setter
public class MeasurUnitDto implements Serializable {
    Long id;
    String measuUnitCode;
    String measuUnitName;
    String measuUnitDianCode;
}
