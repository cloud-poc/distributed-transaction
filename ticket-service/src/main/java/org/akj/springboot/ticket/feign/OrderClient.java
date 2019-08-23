package org.akj.springboot.ticket.feign;

import org.akj.springboot.model.Order;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;

@FeignClient(value = "order-service", path = "/orders")
public interface OrderClient {
    @PutMapping
    public Order update(@RequestBody @Valid Order order);

    @GetMapping("/{orderId}")
    public Order getOrder(@PathVariable @NotEmpty String orderId);
}
