package org.akj.springboot.tx.entity.audit;

import lombok.Data;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Data
@Entity
@Table(name="customers_audit")
public class CustomerAudit implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO,generator = "native")
    @GenericGenerator(
            name = "native",
            strategy = "native"
    )
    private Integer id;

    @Column(nullable = false)
    private String operation;

    @Column(nullable = false)
    private Integer userId;

    @Column(nullable = false)
    private Date createDate;
}
