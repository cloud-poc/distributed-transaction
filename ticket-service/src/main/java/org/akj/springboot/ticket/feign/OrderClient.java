package org.akj.springboot.ticket.feign;

import org.akj.springboot.model.Order;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

import javax.validation.Valid;

@FeignClient(value = "order-service", path = "/orders")
public interface OrderClient {
    @PutMapping
    public Order update(@RequestBody @Valid Order order);
}
