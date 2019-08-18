package org.akj.springboot.tx.service;

import lombok.extern.slf4j.Slf4j;
import org.akj.springboot.tx.entity.Customer;
import org.akj.springboot.tx.repository.CustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;

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

    private Random random = new Random(10l);

    public List<Customer> findAll() {

        List<Customer> customers = repository.findAll();
        return customers != null ? customers : new ArrayList(0);
    }

    @Transactional
    public Customer add(Customer customer) {
        if (customer.getId() == null) {
            Date date = new Date();
            customer.setCreateDate(date);
            customer.setLastUpdateTime(date);
        }

        DefaultTransactionDefinition definition = new DefaultTransactionDefinition();
        definition.setIsolationLevel(TransactionDefinition.ISOLATION_REPEATABLE_READ);
        definition.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRED);
        TransactionStatus transactionStatus = transactionManager.getTransaction(definition);

        try {
            customer = repository.save(customer);
            simulateException();
            transactionManager.commit(transactionStatus);
        } catch (Exception e) {
            transactionManager.rollback(transactionStatus);
            throw e;
        }
//        customer = repository.save(customer);
//        simulateException();

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
