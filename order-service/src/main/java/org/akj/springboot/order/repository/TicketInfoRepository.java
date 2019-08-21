package org.akj.springboot.order.repository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class TicketInfoRepository {
    private static  final String SQL_TICKET_LOCK = "update TicketInfo set count = count - ? where count >= ?";

    @Autowired
    @Qualifier("ticketJdbcTemplate")
    private JdbcTemplate jdbcTemplate;

    public int lock(int ticketCount){
        return jdbcTemplate.update(SQL_TICKET_LOCK, new Object[]{ticketCount,ticketCount});
    }
}
