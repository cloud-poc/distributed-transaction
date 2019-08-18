package org.akj.springboot.dtx.entity;

import lombok.Data;

import javax.persistence.*;

@Entity(name = "customer_order")
@Data
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    @Column(nullable = false,name = "user_id")
    private Integer userId;

    private String title;

    private String detail;

    private int amount;
}