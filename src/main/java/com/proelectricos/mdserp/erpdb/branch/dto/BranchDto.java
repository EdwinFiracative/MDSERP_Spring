package com.proelectricos.mdserp.erpdb.branch.dto;

import com.proelectricos.mdserp.erpdb.client.dto.ClientDto;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * DTO for {@link com.proelectricos.mdserp.erpdb.branch.Branch}
 */
@Getter
@Setter
public class BranchDto implements Serializable {
    Long branchId;
    String branchCode;
    String branchCity;
    String branchAddress;
    ClientDto branchClient;
}
