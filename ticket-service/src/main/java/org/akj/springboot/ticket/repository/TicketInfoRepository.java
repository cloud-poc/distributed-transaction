package org.akj.springboot.ticket.repository;

import org.akj.springboot.ticket.entity.Ticket;
import org.akj.springboot.ticket.entity.TicketInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

public interface TicketInfoRepository extends JpaRepository<TicketInfo, Integer> {

}
