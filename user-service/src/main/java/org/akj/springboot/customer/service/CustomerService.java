package org.akj.springboot.customer.service;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixProperty;
import lombok.extern.slf4j.Slf4j;
import org.akj.springboot.common.exception.BusinessException;
import org.akj.springboot.customer.bean.DepositRequest;
import org.akj.springboot.customer.entity.Customer;
import org.akj.springboot.customer.entity.PaymentHistory;
import org.akj.springboot.customer.feign.OrderClient;
import org.akj.springboot.customer.repository.CustomerRepository;
import org.akj.springboot.customer.repository.PaymentHistoryRepository;
import org.akj.springboot.model.Order;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;

import javax.transaction.Transactional;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Random;

@Service
@Slf4j
public class CustomerService {
    @Autowired
    private CustomerRepository repository;

    @Autowired
    private PaymentHistoryRepository paymentHistoryRepository;

    @Autowired
    private PlatformTransactionManager transactionManager;

    @Autowired
    private JmsTemplate jmsTemplate;

    @Autowired
    private OrderClient orderClient;

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
            LocalDateTime date = LocalDateTime.now();
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

    public Customer findCustomerById(String uid) {
        Optional<Customer> customerOptional = repository.findById(uid);

        if(!customerOptional.isPresent()){
           throw new BusinessException("ERROR-003-002","user does not exists");
        }

        return customerOptional.get();
    }

    @JmsListener(destination = "ticket:order:new")
    @Transactional
    public void paymentForOrder(@Payload Order order){
        // 1.check if it's duplicate order
        if (paymentHistoryRepository.findPaymentHistoryByOrderId(order.getId()) != null){
            log.debug("order {} has been payed", order);
            return;
        }

        // 2. payment & update payment history
        BigDecimal totalAmount = order.getUnitPrice().multiply(new BigDecimal(order.getTicketCount()));
        int status = repository.pay(order.getUserId(),
                totalAmount);
        log.debug("payment status for order: {}", status);
        if(status < 1){
            order.setStatus("PAYMENT_FAILED");
            order.setRemark("PAYMENT_FAILED_WITH_NO_SUFFICIENT_BALANCE");
            orderClient.update(order);
            jmsTemplate.convertAndSend("ticket:order:pay:failed",order);
            return;
        }
        PaymentHistory paymentHistory = new PaymentHistory();
        paymentHistory.setAmount(totalAmount);
        paymentHistory.setCreateDate(order.getCreateDate());
        paymentHistory.setOrderId(order.getId());
        paymentHistory.setUserId(order.getUserId());
        paymentHistoryRepository.save(paymentHistory);

        // 3. update order status and send to next queue for further steps
        order.setStatus("PAYED");
        orderClient.update(order);
        jmsTemplate.convertAndSend("ticket:order:payed", order);
    }

    @Transactional
    public Customer deposit(DepositRequest request) {
        Optional<Customer> option = repository.findById(request.getUserId());
        if(!option.isPresent()){
            throw new BusinessException("ERROR-003-001", "wrong user information provided");
        }

        Customer customer = option.get();
        customer.setBalance(customer.getBalance().add(request.getAmount()));
        customer.setLastUpdateTime(LocalDateTime.now());

        return repository.save(customer);
    }

}
