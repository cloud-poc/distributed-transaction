package org.akj.springboot.order.resource;

import org.akj.springboot.order.entity.Order;
import org.akj.springboot.order.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/orders")
public class OrderResource {

    @Autowired
    private OrderService orderService;

    @PostMapping
    public Order create(@RequestBody @Valid org.akj.springboot.model.Order order) {
        if(order.getCreateDate() == null){
            order.setCreateDate(LocalDateTime.now());
        }
        Order orderBean = orderService.create(order);

        return orderBean;
    }

    @GetMapping("/{uid}")
    public List<Order> getMyOrder(@PathVariable String uid) {
       List<Order> orders = orderService.findByUserId(uid);

        return orders;
    }

    @GetMapping("")
    public List<Order> getAll() {
        return orderService.findAll();
    }

    @PutMapping
    public Order update(@RequestBody @Valid Order order){
        return orderService.update(order);
    }

    @PutMapping("/{orderId}/pay")
    public Map<String,String> payment(@PathVariable String orderId){
        orderService.pay(orderId);

        HashMap<String, String> map = new HashMap<>();
        map.put("message", "submitted payment successfully");

        return map;
    }
}
