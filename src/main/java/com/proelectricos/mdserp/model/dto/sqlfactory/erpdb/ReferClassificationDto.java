package com.proelectricos.mdserp.model.dto.sqlfactory.erpdb;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import lombok.Value;

import java.io.Serializable;

/**
 * DTO for {@link com.proelectricos.mdserp.model.entity.sqlfactory.erpdb.ReferClassification}
 */
@Getter
@Setter
public class ReferClassificationDto implements Serializable {
    Long id;

    ReferClassificationDto referClassFather;

    @NotNull
    @Size(max = 100)
    String referClassName;

    @Size(max = 200)
    String referClassDescription;

    @Size(max = 11)
    String referClassGroupFactory;
}