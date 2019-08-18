package org.akj.springboot.customer.service;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixProperty;
import lombok.extern.slf4j.Slf4j;
import org.akj.springboot.customer.entity.Customer;
import org.akj.springboot.customer.repository.CustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;

import javax.transaction.Transactional;
import java.util.*;

@Service
@Slf4j
public class CustomerService {

    @Autowired
    private CustomerRepository repository;

    @Autowired
    private PlatformTransactionManager transactionManager;

    private Random random = new Random(10l);

    @HystrixCommand(commandKey = "customer-repository-list-all", groupKey = "customer",
            fallbackMethod = "customersListAllFallback", ignoreExceptions = {NullPointerException.class},
            threadPoolProperties = {
                    @HystrixProperty(name = "coreSize", value = "30"),
                    @HystrixProperty(name = "maxQueueSize", value = "101"),
                    @HystrixProperty(name = "keepAliveTimeMinutes", value = "2"),
                    @HystrixProperty(name = "queueSizeRejectionThreshold", value = "15"),
                    @HystrixProperty(name = "metrics.rollingStats.numBuckets", value = "12"),
                    @HystrixProperty(name = "metrics.rollingStats.timeInMilliseconds", value = "1440")
            }
    )
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

    public List<Customer> customersListAllFallback() {
        return new ArrayList<>(0);
    }

    public Customer findCustomerById(Integer uid) {
        Optional<Customer> customerOptional = repository.findById(uid);

        if(!customerOptional.isPresent()){
           throw new RuntimeException("user does not exists");
        }

        return customerOptional.get();
    }
}
