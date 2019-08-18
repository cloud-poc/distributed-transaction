package org.akj.springboot.order.resource;

import org.akj.springboot.order.entity.Order;
import org.akj.springboot.order.repository.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/orders")
public class OrderResource {

    @Autowired
    private OrderRepository orderRepository;

    @PostMapping
    public Order create(@RequestBody Order order) {
        order = orderRepository.save(order);
        return order;
    }

    @GetMapping("/{uid}")
    public List<Order> getMyOrder(@PathVariable Integer uid) {
       List<Order> orders = orderRepository.findByUserId(uid);

        return orders;
    }

    @GetMapping("")
    public List<Order> getAll() {
        return orderRepository.findAll();
    }

}
