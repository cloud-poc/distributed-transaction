package org.akj.springboot.order.entity;

import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;


@Entity(name = "ticket_order")
@Data
public class Order implements Serializable {
    @Id
    private String id;

    @Column(nullable = false,name="user_id")
    private String userId;

    @Column(nullable = false,name="ticket_info_id")
    private String ticketInfoId;

    private String title;

    private String detail;

    private String status;

    @Column(name="ticket_count")
    private int ticketCount;

    @Column(name="unit_price")
    private BigDecimal unitPrice;

    @Column(name="create_date")
    private LocalDateTime createDate;

    private String remark;
}
