package org.akj.springboot.dtx.service;

import org.akj.springboot.dtx.entity.Order;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class OrderServiceTest {
    @Autowired
    private OrderService orderService;
    private Order order = null;

    @BeforeEach
    void setUp() {
        order = new Order();
        order.setTitle("ThinkPad X260");
        order.setDetail("8GB memory, 256 GB SSD");
        order.setUserId(1);
        order.setAmount(10);
    }

    @Test
    void create() {
        orderService.create(order);
    }
}