package org.akj.springboot.ticket.repository;

import org.akj.springboot.ticket.entity.Ticket;
import org.akj.springboot.ticket.entity.TicketInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.persistence.NamedNativeQuery;

public interface TicketInfoRepository extends JpaRepository<TicketInfo, Integer> {

    @Modifying
    @Query(value = "update ticket_info set total=total+ ?2 where id= ?1")
    public int unlock(String ticketInfoId, Integer ticketCount);
}
