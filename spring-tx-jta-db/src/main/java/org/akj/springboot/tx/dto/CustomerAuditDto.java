package org.akj.springboot.tx.dto;

import lombok.Data;
import org.akj.springboot.tx.entity.audit.CustomerAudit;

import java.util.List;

@Data
public class CustomerAuditDto {
    private boolean hasNextPage = false;

    private int pageNo;

    private long totalRecords;

    private List<CustomerAudit> auditLoglist;
}
