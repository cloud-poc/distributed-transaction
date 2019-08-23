package org.akj.springboot.customer.feign;

import org.akj.springboot.common.exception.BusinessException;
import org.akj.springboot.model.Order;
import org.springframework.stereotype.Component;

import javax.validation.Valid;
import java.util.List;

@Component
public class OrderServiceFallback implements OrderClient{
    @Override
    public List<Order> findOrdersByUserId(String uid) {
        throw new BusinessException("ERROR-003-003", "OrderClient#findOrdersByUserId service available");
    }

    @Override
    public Order update(@Valid Order order) {
        throw new BusinessException("ERROR-003-003", "OrderClient#update service available");
    }
}
