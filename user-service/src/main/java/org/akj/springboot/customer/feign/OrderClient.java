package org.akj.springboot.customer.feign;

import org.akj.springboot.customer.model.Order;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@FeignClient(value = "order-service", path = "/orders")
public interface OrderClient {

    @GetMapping("/{uid}")
    public List<Order> findByUserId(@PathVariable("uid") Integer uid);
}
