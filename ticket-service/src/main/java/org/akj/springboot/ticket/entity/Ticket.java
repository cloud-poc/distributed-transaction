package org.akj.springboot.ticket.entity;

import lombok.Data;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
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

    private String orderId;

    private String userId;

    private LocalDateTime createDate;
}
