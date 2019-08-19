package org.akj.springboot.dtx.resource;

import lombok.extern.slf4j.Slf4j;
import org.akj.springboot.dtx.entity.Customer;
import org.akj.springboot.dtx.service.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsTemplate;
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

    @Autowired(required = false)
    private JmsTemplate jmsTemplate;

    @GetMapping("/customers")
    public List<Customer> list() {
        return service.findAll();
    }

    @PostMapping("/customers")
    public Customer add(@RequestBody Customer customer) {
        return service.add(customer);
    }

    //@Transactional
    @PostMapping("/message/customers")
    public void addByListener(@RequestBody Customer customer) {
        log.debug("invoke {}.{}", CustomerResource.class, "addByListener");
        jmsTemplate.convertAndSend("customer:msg:new", customer);
        log.debug("finished {}.{}", CustomerResource.class, "addByListener");
    }
}
