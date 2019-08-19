package org.akj.springboot.dtx.service;

import lombok.extern.slf4j.Slf4j;
import org.akj.springboot.dtx.entity.Customer;
import org.akj.springboot.dtx.repository.CustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;

@Service
@Slf4j
public class CustomerService {

    @Autowired
    private CustomerRepository repository;

    @Autowired(required = false)
    private JmsTemplate jmsTemplate;

    private Random random = new Random();

    public List<Customer> findAll() {

        List<Customer> customers = repository.findAll();
        return customers != null ? customers : new ArrayList(0);
    }

    @Transactional
    @JmsListener(destination = "customer:msg:new")
    public void createByListener(@Payload Customer customer) {
        log.debug("invoke {}.{}", CustomerService.class, "createByListener");
        if (customer.getId() == null) {
            Date date = new Date();
            customer.setCreateDate(date);
            customer.setLastUpdateTime(date);
        }
        // FIXME idempotent issue for redelivery cases

        simulateException();
        customer = repository.save(customer);
        simulateException();
        jmsTemplate.convertAndSend("customer:msg:reply", customer);
        simulateException();
        log.debug("finished {}.{}", CustomerService.class, "createByListener");
    }

    @Transactional
    public Customer add(Customer customer) {
        if (customer.getId() == null) {
            Date date = new Date();
            customer.setCreateDate(date);
            customer.setLastUpdateTime(date);
        }

        customer = repository.save(customer);
        simulateException();
        jmsTemplate.convertAndSend("customer:msg:new", customer.toString());

        return customer;
    }

    private void simulateException() {
        int i = random.nextInt(20);
        log.info("random number for simulate exception is {}", i);
        if (i <= 5) {
            throw new RuntimeException("simulate service exception");
        }
    }
}
