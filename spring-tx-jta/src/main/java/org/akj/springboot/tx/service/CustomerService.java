package org.akj.springboot.tx.service;

import lombok.extern.slf4j.Slf4j;
import org.akj.springboot.tx.entity.Customer;
import org.akj.springboot.tx.repository.CustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessagePostProcessor;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;
import org.springframework.transaction.PlatformTransactionManager;

import javax.jms.JMSException;
import javax.jms.Message;
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

    @Autowired
    private PlatformTransactionManager transactionManager;

    @Autowired
    private JmsTemplate jmsTemplate;

    private Random random = new Random(10l);

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
        customer = repository.save(customer);

        jmsTemplate.convertAndSend("customer:msg:reply", customer);
        log.debug("finished {}.{}", CustomerService.class, "createByListener");
    }

    @Transactional
    public Customer add(Customer customer) {
        if (customer.getId() == null) {
            Date date = new Date();
            customer.setCreateDate(date);
            customer.setLastUpdateTime(date);
        }

//        DefaultTransactionDefinition definition = new DefaultTransactionDefinition();
//        definition.setIsolationLevel(TransactionDefinition.ISOLATION_REPEATABLE_READ);
//        definition.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRED);
//        definition.setTimeout(15);
//        TransactionStatus transactionStatus = transactionManager.getTransaction(definition);

//        try {
//            customer = repository.save(customer);
//            simulateException();
//            transactionManager.commit(transactionStatus);
//        } catch (Exception e) {
//            transactionManager.rollback(transactionStatus);
//            throw e;
//        }
        customer = repository.save(customer);
        simulateException();
        jmsTemplate.convertAndSend("customer:msg:new", customer.toString());

        return customer;
    }

    private void simulateException() {
        int i = random.nextInt(10);
        log.info("random number for simulate exception is {}", i);
        if (i % 2 == 0) {
            throw new RuntimeException("simulate service exception");
        }
    }
}
