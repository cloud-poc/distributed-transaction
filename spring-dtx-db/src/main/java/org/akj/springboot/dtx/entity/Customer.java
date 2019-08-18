package org.akj.springboot.dtx.entity;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class Customer {
    private Integer id;

    private String name;

    private BigDecimal amount;
}
