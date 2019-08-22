package org.akj.springboot.customer.bean;

import lombok.Data;

import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

@Data
public class DepositRequest {
    @NotNull
    private String userId;

    @NotNull
    @DecimalMin("0.01")
    private BigDecimal amount;
}
