package org.akj.springboot.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import org.springframework.lang.NonNull;

import javax.validation.constraints.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class Order implements Serializable {
    private String id;

    @NotNull
    private String userId;

    @NotNull
    private String ticketInfoId;

    private String title;

    private String detail;

    private String status;

    @NotNull
    @Min(1)
    @Max(5)
    private int ticketCount;

    @NotNull
    @DecimalMin("0.01")
    @DecimalMax("1000000")
    private BigDecimal unitPrice;

    private LocalDateTime createDate;

    private String remark;
}