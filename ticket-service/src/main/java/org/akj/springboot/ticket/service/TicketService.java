package org.akj.springboot.ticket.service;

import lombok.extern.slf4j.Slf4j;
import org.akj.springboot.model.Order;
import org.akj.springboot.ticket.entity.Ticket;
import org.akj.springboot.ticket.feign.OrderClient;
import org.akj.springboot.ticket.repository.TicketInfoRepository;
import org.akj.springboot.ticket.repository.TicketRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
public class TicketService {
    @Autowired
    private TicketRepository ticketRepository;

    @Autowired
    private TicketInfoRepository ticketInfoRepository;

    @Autowired
    private JmsTemplate jmsTemplate;

    @Autowired
    private OrderClient orderClient;

    @JmsListener(destination = "ticket:order:payed")
    @Transactional
    public void generateTicket(@Payload Order order) {
        log.info("generate tickets for order {}", order);

        //1. check if duplicate ticket generation request
        List<Ticket> tickets = ticketRepository.findTicketsByOrderId(order.getId());
        if (tickets != null && tickets.size() > 0) {
            log.debug("tickets already generated for order {}",  order);
            return ;
        }

        tickets = new ArrayList<>(order.getTicketCount());
        for(int i = 0; i < order.getTicketCount(); i++){
            Ticket ticket = new Ticket();
            ticket.setUserId(order.getUserId());
            ticket.setOrderId(order.getId());
            ticket.setCreateDate(order.getCreateDate());
            tickets.add(ticket);
        }

        ticketRepository.saveAll(tickets);

        // 2. update order status and send to queue
        order.setStatus("TICKET_GENERATED");
        orderClient.update(order);
        jmsTemplate.convertAndSend("ticket:order:generated", order);
        log.info("finished tickets generation for order {}", order);
    }

    @JmsListener(destination = "ticket:order:pay:failed")
    @Transactional
    public void paymentFailHandler(@Payload Order order){
        order.setStatus("PAYMENT_PENDING");
        orderClient.update(order);
        log.debug("update order status to PAYMENT_PENDING for {}",order);
    }
}
