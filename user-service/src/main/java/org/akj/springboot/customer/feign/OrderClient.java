package org.akj.springboot.customer.feign;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixProperty;
import org.akj.springboot.model.Order;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

import javax.validation.Valid;
import java.util.List;

@FeignClient(value = "order-service", path = "/orders", fallbackFactory = OrderServiceFallbackFactory.class)
public interface OrderClient {
    @GetMapping("/{uid}")
    public List<Order> findOrdersByUserId(@PathVariable("uid") String uid);

    @PutMapping
    public Order update(@RequestBody @Valid Order order);
}
