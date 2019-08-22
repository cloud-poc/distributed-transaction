package org.akj.springboot.ticket.repository;

import org.akj.springboot.ticket.entity.Ticket;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface TicketRepository extends JpaRepository<Ticket, Integer> {
    @Query("select ticket from Ticket ticket where ticket.orderId=?1")
    public List<Ticket> findTicketsByOrderId(String orderId);
}
