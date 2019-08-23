package org.akj.springboot.order.model;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import java.time.LocalDateTime;

@Data
public class TicketInfo {
    @Id
    private String id;

    private String name;

    private String description;

    private Integer total;

    private LocalDateTime lastUpdateDate;

    private LocalDateTime createDate;
}
