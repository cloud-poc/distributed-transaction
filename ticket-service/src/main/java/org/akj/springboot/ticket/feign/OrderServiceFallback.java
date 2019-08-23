package org.akj.springboot.ticket.feign;

import org.akj.springboot.common.exception.BusinessException;
import org.akj.springboot.model.Order;
import org.springframework.stereotype.Component;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import java.util.List;

@Component
public class OrderServiceFallback implements OrderClient{
    @Override
    public Order update(@Valid Order order) {
        throw new BusinessException("ERROR-002-003", "OrderClient#update service available");
    }

    @Override
    public Order getOrder(@NotEmpty String orderId) {
        throw new BusinessException("ERROR-002-003", "OrderClient#update service available");
    }
}
