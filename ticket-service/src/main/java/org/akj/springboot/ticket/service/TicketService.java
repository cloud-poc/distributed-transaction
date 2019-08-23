package org.akj.springboot.ticket.service;

import lombok.extern.slf4j.Slf4j;
import org.akj.springboot.common.exception.BusinessException;
import org.akj.springboot.model.Order;
import org.akj.springboot.ticket.entity.Ticket;
import org.akj.springboot.ticket.entity.TicketLockHistory;
import org.akj.springboot.ticket.feign.OrderClient;
import org.akj.springboot.ticket.repository.TicketInfoRepository;
import org.akj.springboot.ticket.repository.TicketLockHistoryRepository;
import org.akj.springboot.ticket.repository.TicketRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class TicketService {
    @Autowired
    private TicketRepository ticketRepository;

    @Autowired
    private TicketInfoRepository ticketInfoRepository;

    @Autowired
    private TicketLockHistoryRepository ticketLockHistoryRepository;

    @Autowired
    private JmsTemplate jmsTemplate;

    @Autowired
    private OrderClient orderClient;

    @JmsListener(destination = "ticket:order:payed")
    @Transactional
    public void generateTicket(@Payload Order order) {
        log.info("generate tickets for order {}", order);
        if (!checkIfTicketGenerated(order)) {
            doGenerateTickets(order);
            doUpdateOrderStatusAfterGeneration(order);
        }
    }

    private void doUpdateOrderStatusAfterGeneration(Order order) {
        //update order status and send to queue
        order.setStatus("TICKET_GENERATED");
        orderClient.update(order);
        jmsTemplate.convertAndSend("ticket:order:generated", order);
        log.info("finished tickets generation for order {}", order);
    }

    private boolean checkIfTicketGenerated(Order order) {
        boolean flag = false;
        //1. check if duplicate ticket generation request
        List<Ticket> tickets = ticketRepository.findTicketsByOrderId(order.getId());
        if (tickets != null && tickets.size() > 0) {
            // if any dirty data
            if (order.getTicketCount() == tickets.size()) {
                log.debug("tickets already generated for order {}", order);
                flag = true;
            } else {
                // remove dirty data
                tickets.removeAll(tickets);
            }
        }

        return flag;
    }

    private void doGenerateTickets(Order order) {
        List<Ticket> tickets;
        tickets = new ArrayList<>(order.getTicketCount());
        for (int i = 0; i < order.getTicketCount(); i++) {
            Ticket ticket = new Ticket();
            ticket.setUserId(order.getUserId());
            ticket.setOrderId(order.getId());
            ticket.setTicketInfoId(order.getTicketInfoId());
            ticket.setCreateDate(order.getCreateDate());
            tickets.add(ticket);
        }

        tickets = ticketRepository.saveAll(tickets);
    }

    @JmsListener(destination = "ticket:order:pay:failed")
    @Transactional
    public void paymentFailHandler(@Payload Order order) {
        order.setStatus("PAYMENT_PENDING");
        orderClient.update(order);
        log.debug("update order status to PAYMENT_PENDING for {}", order);
    }

    @JmsListener(destination = "ticket:order:pay:failed:finish")
    @Transactional
    public void rollback(@Payload Order order) {
        log.debug("unlock ticket for failed order:{}", order);
        int result = ticketInfoRepository.unlock(order.getTicketInfoId(), order.getTicketCount());
        if (result < 1) {
            log.debug("unlock ticket failed for {}", order);
            order.setStatus("UNLOCK_FAILED");
            orderClient.update(order);
        }

        TicketLockHistory ticketLock = new TicketLockHistory();
        // unlock history
        ticketLock.setCount(0 - order.getTicketCount());
        ticketLock.setCreateDate(LocalDateTime.now());
        ticketLock.setOrderId(order.getId());
        ticketLock.setTicketInfoId(order.getTicketInfoId());
        ticketLockHistoryRepository.save(ticketLock);
    }

    public Ticket getTicketDetail(Integer id) {
        Optional<Ticket> ticketOptional = ticketRepository.findById(id);
        if (!ticketOptional.isPresent()) {
            throw new BusinessException("Error-002-001", "no ticket found by given ticket id:" + id);
        }

        return ticketOptional.get();
    }

    @Transactional
    public List<Ticket> generateTicket(String orderId) {
        Order order = orderClient.getOrder(orderId);
        log.info("generate tickets for order {}", order);
        if (!checkIfTicketGenerated(order)) {
            doGenerateTickets(order);
            doUpdateOrderStatusAfterGeneration(order);
        }

        return ticketRepository.findTicketsByOrderId(orderId);
    }
}
