package org.akj.springboot.dtx.bean;

import lombok.Data;
import org.akj.springboot.dtx.entity.Customer;
import org.akj.springboot.dtx.entity.Order;

import java.util.List;
@Data
public class UserInfoResponse {
    private Customer user;

    private List<Order> orders;
}
