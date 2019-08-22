package org.akj.springboot.customer.bean;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class DepositResponse {
    private String srNo;

    private String userId;

    private BigDecimal balance;

    private LocalDateTime localDateTime;
}
