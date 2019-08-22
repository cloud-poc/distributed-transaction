package org.akj.springboot.order.service;

import com.fasterxml.uuid.Generators;
import com.fasterxml.uuid.impl.TimeBasedGenerator;
import lombok.extern.slf4j.Slf4j;
import org.akj.springboot.common.exception.BusinessException;
import org.akj.springboot.order.entity.Order;
import org.akj.springboot.order.entity.TicketLockHistory;
import org.akj.springboot.order.repository.OrderRepository;
import org.akj.springboot.order.repository.TicketInfoRepository;
import org.akj.springboot.order.repository.TicketLockHistoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class OrderService {
    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private TicketLockHistoryRepository ticketLockRepository;

    @Autowired
    private TicketInfoRepository ticketInfoRepository;

    @Autowired
    private JmsTemplate jmsTemplate;

    @Transactional
    public Order create(org.akj.springboot.model.Order order) {
        TimeBasedGenerator timeBasedGenerator = Generators.timeBasedGenerator();
        if (order.getId() == null) {
            order.setId(timeBasedGenerator.generate().toString());
        }

        // if no tickets, throw no ticket exception
        int lock = ticketInfoRepository.lock(order.getTicketCount());
        if (lock < 1) {
            throw new BusinessException("ERROR-20-001", "no ticket available");
        }

        // save ticket lock history
        TicketLockHistory ticketLock = new TicketLockHistory();
        ticketLock.setCount(order.getTicketCount());
        ticketLock.setCreateDate(LocalDateTime.now());
        ticketLock.setId(timeBasedGenerator.generate().toString());
        ticketLock.setOrderId(order.getId());
        ticketLockRepository.save(ticketLock);

        order.setStatus("NEW");
        Order orderBean = constructOrderBean(order);
        orderBean = orderRepository.save(orderBean);

        // start the order flow
        jmsTemplate.convertAndSend("ticket:order:new", order);

        return orderBean;
    }

    private Order constructOrderBean(org.akj.springboot.model.Order order) {
        Order o = new Order();
        o.setId(order.getId());
        o.setStatus(order.getStatus());
        o.setCreateDate(order.getCreateDate());
        o.setDetail(order.getDetail());
        o.setTicketCount(order.getTicketCount());
        o.setTitle(order.getTitle());
        o.setUnitPrice(order.getUnitPrice());
        o.setUserId(order.getUserId());
        o.setRemark(order.getRemark());

        return o;
    }

    private org.akj.springboot.model.Order constructOrderDTO(Order order) {
        org.akj.springboot.model.Order o = new org.akj.springboot.model.Order();
        o.setId(order.getId());
        o.setStatus(order.getStatus());
        o.setCreateDate(order.getCreateDate());
        o.setDetail(order.getDetail());
        o.setTicketCount(order.getTicketCount());
        o.setTitle(order.getTitle());
        o.setUnitPrice(order.getUnitPrice());
        o.setUserId(order.getUserId());
        o.setRemark(order.getRemark());

        return o;
    }

    public List<Order> findByUserId(String uid) {
        return orderRepository.findByUserId(uid);
    }

    public List<Order> findAll() {
        return orderRepository.findAll();
    }

    @JmsListener(destination = "ticket:order:generated")
    public void orderFinished(@Payload org.akj.springboot.model.Order order) {
        log.info("finished order {}", order);

        Order orderBean = constructOrderBean(order);
        orderBean.setStatus("COMPLETED");
        orderRepository.save(orderBean);
    }

    public Order update(Order order) {
        return orderRepository.save(order);
    }

    @Scheduled(fixedDelay = 30000)
    @Transactional
    public void checkPaymentStatusJob() {
        log.info("scheduled job started - check pending payment orders");
        // 1. get all un-payed orders
        List<Order> orders = orderRepository.findOrderByStatus("PAYMENT_PENDING");
        log.debug("found {} orders which in PAYMENT_PENDING status", orders.size());

        orders.forEach(o -> {
            //2. check if order's live time is larger than 3 mins, if yes, marked as failed order
            if (o.getCreateDate().plusMinutes(3).isBefore(LocalDateTime.now())) {
                o.setStatus("FAILED");
                orderRepository.save(o);

                //constructOrderDTO
                org.akj.springboot.model.Order orderDTO = constructOrderDTO(o);
                jmsTemplate.convertAndSend("ticket:order:pay:failed:finish", orderDTO);
                log.debug("payment timeout, mark order as failed - {}", orderDTO);
            }
        });
    }

    @Transactional
    public void pay(String orderId) {
        Optional<Order> optional = orderRepository.findById(orderId);
        if (!optional.isPresent()) {
            throw new BusinessException("ERROR-003-001", "no order found by given orderId:" + orderId);
        }

        Order order = optional.get();
        org.akj.springboot.model.Order o = constructOrderDTO(order);

        jmsTemplate.convertAndSend("ticket:order:new",o);
        log.debug("sent order to ticket:order:new queue for payment - {}", o);
    }
}
