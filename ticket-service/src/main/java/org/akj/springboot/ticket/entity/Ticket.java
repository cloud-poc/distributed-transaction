package org.akj.springboot.ticket.entity;

import lombok.Data;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Data
public class Ticket {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO,generator = "native")
    @GenericGenerator(
            name = "native",
            strategy = "native"
    )
    private Integer id;

    @Column(nullable = false)
    private String orderId;

    @Column(nullable = false)
    private String userId;

    @Column(nullable = false,name="ticket_info_id")
    private String ticketInfoId;

    private LocalDateTime createDate;
}
