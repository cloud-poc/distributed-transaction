package org.akj.springboot.order.model;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import java.time.LocalDateTime;

@Data
public class TicketLockHistory {
    private Integer id;

    private int count;

    private String orderId;

    private String ticketInfoId;

    private LocalDateTime  createDate;
}
