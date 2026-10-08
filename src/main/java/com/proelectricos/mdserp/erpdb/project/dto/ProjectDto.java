package com.proelectricos.mdserp.erpdb.project.dto;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * DTO for {@link com.proelectricos.mdserp.erpdb.project.Project}
 */
@Getter
@Setter
public class ProjectDto implements Serializable {
    Long projeId;
    String projeName;
}
