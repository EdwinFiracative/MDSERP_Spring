package com.proelectricos.mdserp.model.dto.sqlfactory.erpdb;

import com.proelectricos.mdserp.model.entity.sqlfactory.erpdb.ReferClassification;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import lombok.Value;

import java.io.Serializable;
import java.util.LinkedHashSet;
import java.util.Set;

/**
 * DTO for {@link com.proelectricos.mdserp.model.entity.sqlfactory.erpdb.ReferClassification}
 */
@Getter
@Setter
public class ReferClassificationSonsDto implements Serializable {
    Long id;
    @NotNull
    @Size(max = 100)
    String referClassName;
    @Size(max = 200)
    String referClassDescription;
    @Size(max = 11)
    String referClassGroupFactory;
    Set<ReferClassificationSonsDto> ReferClassificationSons = new LinkedHashSet<>();
}