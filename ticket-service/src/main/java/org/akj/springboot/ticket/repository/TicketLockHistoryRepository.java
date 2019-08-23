package org.akj.springboot.ticket.repository;

import org.akj.springboot.ticket.entity.TicketInfo;
import org.akj.springboot.ticket.entity.TicketLockHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface TicketLockHistoryRepository extends JpaRepository<TicketLockHistory, Integer> {

}
