package org.akj.springboot.dtx.resource;

import lombok.extern.slf4j.Slf4j;
import org.akj.springboot.dtx.entity.Order;
import org.akj.springboot.dtx.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/orders")
@Slf4j
public class OrderResource {

   @Autowired
   private OrderService orderService;

    @PostMapping
    public void create(@RequestBody  Order order) {
        orderService.create(order);
    }

}
