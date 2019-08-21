package org.akj.springboot.order.entity;

import lombok.Data;

import javax.persistence.*;
import java.time.LocalDateTime;


@Entity(name = "ticket_order")
@Data
public class Order {
    @Id
    private String id;

    @Column(nullable = false,name="user_id")
    private Integer userId;

    private String title;

    private String detail;

    private String status;

    @Column(name="ticket_count")
    private int ticketCount;

    @Column(name="unit_price")
    private int unitPrice;

    @Column(name="create_date")
    private LocalDateTime createDate;
}
