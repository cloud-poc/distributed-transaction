package org.akj.springboot.customer.repository;

import org.akj.springboot.customer.entity.Customer;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;
import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.util.Base64;

@SpringBootTest
class CustomerRepositoryTest {

    @Autowired
    private CustomerRepository repository;

    @Test
    public void test() {
        Customer customer = new Customer();
        customer.setName("Jamie Zhang");
        customer.setUserName("Jamie");
        SecureRandom secureRandom = new SecureRandom();
        customer.setPassword(Base64.getEncoder().encodeToString(secureRandom.generateSeed(6)));
        customer.setBalance(new BigDecimal(1000));
        LocalDateTime now = LocalDateTime.now();
        customer.setCreateDate(now);
        customer.setLastUpdateTime(now);

        repository.save(customer);
    }

}