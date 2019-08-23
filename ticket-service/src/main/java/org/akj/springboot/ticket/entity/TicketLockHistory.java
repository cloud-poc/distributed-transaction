package org.akj.springboot.ticket.entity;

import lombok.Data;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity(name = "ticket_lock_history")
@Data
public class TicketLockHistory {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO,generator = "native")
    @GenericGenerator(
            name = "native",
            strategy = "native"
    )
    private Integer id;

    private int count;

    @Column(nullable = false,name = "order_id")
    private String orderId;

    @Column(nullable = false,name="ticket_info_id")
    private String ticketInfoId;

    @Column(name="create_date")
    private LocalDateTime  createDate;
}
