package org.akj.springboot.order.entity;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import java.time.LocalDateTime;

//@Entity(name = "ticket_lock")
@Data
public class TicketLockHistory {
    @Id
    private String id;

    private int count;

    @Column(nullable = false,unique = true,name = "order_id")
    private String orderId;

    @Column(name="create_date")
    private LocalDateTime  createDate;
}
