package org.akj.springboot.tx.resource;

import lombok.extern.slf4j.Slf4j;
import org.akj.springboot.tx.entity.customer.Customer;
import org.akj.springboot.tx.service.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@Slf4j
public class CustomerResource {

    @Autowired
    private CustomerService service;

    @GetMapping("/customers")
    public List<Customer> list() {
        return service.findAll();
    }

    @PostMapping("/customers")
    public Customer add(@RequestBody Customer customer) {
        return service.add(customer);
    }

}
