package org.akj.springboot.ticket.resource;

import org.akj.springboot.ticket.entity.Ticket;
import org.akj.springboot.ticket.service.TicketService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("tickets")
public class TicketResource {
    @Autowired
    private TicketService ticketService;

    @GetMapping("/{ticketId}")
    public Ticket getTicketDetail(@PathVariable("ticketId") Integer id) {
        Ticket ticket = ticketService.getTicketDetail(id);
        return ticket;
    }

    @PostMapping("/{orderId}/generate")
    public List<Ticket> generate(@PathVariable String orderId) {
        return ticketService.generateTicket(orderId);
    }
}
