package org.akj.springboot.ticket.entity;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import java.time.LocalDateTime;

@Entity(name="ticket_info")
@Data
public class TicketInfo {
    @Id
    private String id;

    @Column(unique = true)
    private String name;

    private String description;

    private Integer total;

    @Column(name = "last_update_date")
    private LocalDateTime lastUpdateDate;

    @Column(name = "create_date")
    private LocalDateTime createDate;
}
