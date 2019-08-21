package org.akj.springboot.order.service;

import com.fasterxml.uuid.Generators;
import com.fasterxml.uuid.impl.TimeBasedGenerator;
import org.akj.springboot.common.exception.BusinessException;
import org.akj.springboot.order.entity.Order;
import org.akj.springboot.order.entity.TicketLock;
import org.akj.springboot.order.repository.OrderRepository;
import org.akj.springboot.order.repository.TicketInfoRepository;
import org.akj.springboot.order.repository.TicketLockRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class OrderService {
    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private TicketLockRepository ticketLockRepository;

    @Autowired
    private TicketInfoRepository ticketInfoRepository;

    @Autowired
    private JmsTemplate jmsTemplate;

    @Transactional
    public Order create(Order order) {
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
        TicketLock ticketLock = new TicketLock();
        ticketLock.setCount(order.getTicketCount());
        ticketLock.setCreateDate(LocalDateTime.now());
        ticketLock.setId(timeBasedGenerator.generate().toString());
        ticketLock.setOrderId(order.getId());
        ticketLockRepository.save(ticketLock);

        order.setStatus("NEW");
        orderRepository.save(order);

        // start the order flow
        jmsTemplate.convertAndSend("ticket:order:new", order);

        return order;
    }

    public List<Order> findByUserId(Integer uid) {
        return orderRepository.findByUserId(uid);
    }

    public List<Order> findAll() {
        return orderRepository.findAll();
    }
}
