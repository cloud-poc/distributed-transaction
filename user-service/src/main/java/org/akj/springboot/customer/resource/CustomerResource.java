package org.akj.springboot.customer.resource;

import org.akj.springboot.customer.bean.UserInfo;
import org.akj.springboot.customer.entity.Customer;
import org.akj.springboot.customer.feign.OrderClient;
import org.akj.springboot.customer.model.Order;
import org.akj.springboot.customer.service.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users")
public class CustomerResource {

    @Autowired
    private CustomerService service;

    @Autowired
    private OrderClient client;

    @GetMapping
    public List<Customer> list() {
        return service.findAll();
    }

    @PostMapping
    public Customer add(@RequestBody Customer customer) {
        return service.add(customer);
    }

    @GetMapping("/{uid}")
    public UserInfo userInfo(@PathVariable("uid") Integer uid){
        UserInfo userInfo = new UserInfo();
        Customer u = service.findCustomerById(uid);
        userInfo.setUser(u);

        List<Order> orders = client.findByUserId(uid);
        userInfo.setOrders(orders);

        return userInfo;
    }
}
