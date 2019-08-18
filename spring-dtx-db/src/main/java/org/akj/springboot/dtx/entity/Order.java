package org.akj.springboot.dtx.entity;

import lombok.Data;

@Data
public class Order {
    private Integer id;

    private Integer userId;

    private String title;

    private String detail;

    private int amount;
}
