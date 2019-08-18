package org.akj.springboot.dtx.service;

import lombok.extern.slf4j.Slf4j;
import org.akj.springboot.dtx.bean.UserInfoResponse;
import org.akj.springboot.dtx.entity.Customer;
import org.akj.springboot.dtx.entity.Order;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@Service
@Slf4j
public class UserService {

    private static final String SQL_FIND_USER_BY_ID = "select * from customer where id=?";
    private static final String SQL_FIND_ORDERS_BY_USER_ID = "select * from customer_order where user_id=?";
    @Autowired
    @Qualifier("userJdbcTemplate")
    private JdbcTemplate userJdbcTemplate;
    @Autowired
    @Qualifier("orderJdbcTemplate")
    private JdbcTemplate orderJdbcTemplate;

    public UserInfoResponse getUserInfo(@PathVariable Integer uid) {
        UserInfoResponse userInfoResponse = new UserInfoResponse();

        // 1. get user info
        List<Customer> customers = userJdbcTemplate.query(SQL_FIND_USER_BY_ID, new Object[]{uid}, new BeanPropertyRowMapper<Customer>(Customer.class));
        userInfoResponse.setUser(customers.get(0));

        // 2. get order list
        List<Order> orders = orderJdbcTemplate.query(SQL_FIND_ORDERS_BY_USER_ID, new Object[]{uid}, new BeanPropertyRowMapper<Order>(Order.class));
        userInfoResponse.setOrders(orders);

        //TODO error handle

        return userInfoResponse;
    }
}
