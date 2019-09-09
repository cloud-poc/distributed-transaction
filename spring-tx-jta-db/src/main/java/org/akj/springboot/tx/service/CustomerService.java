package org.akj.springboot.tx.service;

import lombok.extern.slf4j.Slf4j;
import org.akj.springboot.tx.entity.audit.CustomerAudit;
import org.akj.springboot.tx.entity.audit.CustomerAuditRepository;
import org.akj.springboot.tx.entity.customer.Customer;
import org.akj.springboot.tx.entity.customer.CustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.security.SecureRandom;
import java.util.*;

@Service
@Slf4j
public class CustomerService {

    @Autowired
    private CustomerRepository repository;

    @Autowired
    private CustomerAuditRepository customerAuditRepository;

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
            customer.setPassword(Base64.getEncoder().encodeToString(new SecureRandom().generateSeed(6)));
        }

        // 1, create customer
        customer = repository.save(customer);
        // simulateException();

        //2, update audit log
        CustomerAudit customerAudit = new CustomerAudit();
        customerAudit.setCreateDate(customer.getCreateDate());
        customerAudit.setOperation("CREATE_USER");
        customerAudit.setUserId(customer.getId());
        customerAuditRepository.save(customerAudit);

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
