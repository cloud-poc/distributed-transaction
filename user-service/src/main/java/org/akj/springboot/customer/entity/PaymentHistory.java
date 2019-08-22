package org.akj.springboot.customer.entity;

import lombok.Data;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity(name = "payment_history")
@Data
public class PaymentHistory {
    @Id
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "uuid2")
    private String id;

    private String orderId;

    private BigDecimal amount;

    private String userId;

    private LocalDateTime createDate;
}
