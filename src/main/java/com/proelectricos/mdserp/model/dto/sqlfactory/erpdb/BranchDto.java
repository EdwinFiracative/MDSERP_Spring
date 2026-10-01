package com.proelectricos.mdserp.model.dto.sqlfactory.erpdb;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * DTO for {@link com.proelectricos.mdserp.model.entity.sqlfactory.erpdb.Branch}
 */
@Getter
@Setter
public class BranchDto implements Serializable {
    Long id;
    ClientDto branchClient;
    String branchCode;
    String branchCity;
    String branchAddress;
}
