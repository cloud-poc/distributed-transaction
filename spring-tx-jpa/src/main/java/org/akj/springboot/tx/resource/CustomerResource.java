package org.akj.springboot.tx.resource;

import org.akj.springboot.tx.entity.Customer;
import org.akj.springboot.tx.service.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/customers")
public class CustomerResource {

    @Autowired
    private CustomerService service;

    @GetMapping
    public List<Customer> list(){
        return service.findAll();
    }

    @PostMapping
    public Customer add(@RequestBody  Customer customer){
        return service.add(customer);
    }
}
