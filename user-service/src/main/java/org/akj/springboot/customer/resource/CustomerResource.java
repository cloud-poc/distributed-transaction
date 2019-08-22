package org.akj.springboot.customer.resource;

import org.akj.springboot.customer.bean.DepositRequest;
import org.akj.springboot.customer.bean.DepositResponse;
import org.akj.springboot.customer.bean.UserInfo;
import org.akj.springboot.customer.entity.Customer;
import org.akj.springboot.customer.feign.OrderClient;
import org.akj.springboot.customer.service.CustomerService;
import org.akj.springboot.model.Order;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/users")
public class CustomerResource {

    @Autowired
    private CustomerService service;

    @Autowired
    private OrderClient client;

    @GetMapping
    public List<Customer> list() {
        return service.findAll();
    }

    @PostMapping
    public Customer add(@RequestBody Customer customer) {
        return service.add(customer);
    }

    @GetMapping("/{uid}")
    public UserInfo userInfo(@PathVariable("uid") String uid) {
        UserInfo userInfo = new UserInfo();
        Customer u = service.findCustomerById(uid);
        userInfo.setUser(u);

        List<Order> orders = client.findByUserId(uid);
        userInfo.setOrders(orders);

        return userInfo;
    }

    @PutMapping("/{uid}/deposit")
    public DepositResponse deposit(@RequestBody @Valid DepositRequest request, @PathVariable("uid") String userId){
        request.setUserId(userId);

        Customer customer = service.deposit(request);
        DepositResponse response = new DepositResponse();
        response.setBalance(customer.getBalance());
        response.setLocalDateTime(customer.getLastUpdateTime());
        response.setSrNo(UUID.randomUUID().toString());
        response.setUserId(customer.getId());

        return response;
    }
}
