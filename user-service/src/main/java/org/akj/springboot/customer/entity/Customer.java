package org.akj.springboot.customer.entity;

import lombok.Data;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Date;

@Data
@Entity
public class Customer {
    @Id
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "uuid2")
    private String id;

    private String name;

    @Column(unique = true)
    private String userName;

    private String password;

    private BigDecimal balance;

    private LocalDateTime createDate;

    private LocalDateTime lastUpdateTime;
}
