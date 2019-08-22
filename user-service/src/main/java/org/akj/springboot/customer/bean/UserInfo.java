package org.akj.springboot.customer.bean;

import lombok.Data;
import org.akj.springboot.customer.entity.Customer;
import org.akj.springboot.model.Order;

import java.util.List;

@Data
public class UserInfo {
    private Customer user;

    private List<Order> orders;
}
