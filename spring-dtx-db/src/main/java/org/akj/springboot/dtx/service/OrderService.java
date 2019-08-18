package org.akj.springboot.dtx.service;

import lombok.extern.slf4j.Slf4j;
import org.akj.springboot.dtx.entity.Order;
import org.akj.springboot.dtx.repository.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Random;

@Service
@Slf4j
public class OrderService {

    private static final String SQL_CREATE_ORDER = "insert into customer_order (title,detail,user_id,amount) values(?,?,?,?)";
    private static final String SQL_PAYMENT_FOR_ORDER = "update customer set amount = amount-? where id=? and amount >= ?";
    @Autowired
    @Qualifier("userJdbcTemplate")
    private JdbcTemplate userJdbcTemplate;
//    @Autowired
//    @Qualifier("orderJdbcTemplate")
//    private JdbcTemplate orderJdbcTemplate;

    @Autowired
    private OrderRepository orderRepository;

    @Transactional
    public void create(Order order) {
        /*int result = orderJdbcTemplate.update(SQL_CREATE_ORDER, order.getTitle(), order.getDetail(), order.getUserId(), order.getAmount());
        if(result < 1){
            log.error("order create failed: {}", order);
            throw new RuntimeException("order create failed");
        }*/
        orderRepository.save(order);
        simulateException();

        int result = userJdbcTemplate.update(SQL_PAYMENT_FOR_ORDER, order.getAmount(),order.getUserId(), order.getAmount());
        if(result < 1){
            log.error("payment for order failed: {}", order);
            throw new RuntimeException("payment for order failed");
        }
        simulateException();
    }

    private void simulateException() {
        Random random = new Random();
        int i = random.nextInt(10);
        log.info("random number for simulate exception is {}", i);
        if (i % 2 == 0) {
            throw new RuntimeException("simulate service exception");
        }
    }

}
