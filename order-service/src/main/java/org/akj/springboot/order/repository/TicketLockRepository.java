package org.akj.springboot.order.repository;

import org.akj.springboot.order.entity.TicketLock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class TicketLockRepository {

    private static final String SQL_TICKET_LOCK_SAVE = "insert into ticket_lock(id,count,order_id,create_date) values (?,?,?,?)";

    @Autowired
    @Qualifier("ticketJdbcTemplate")
    private JdbcTemplate jdbcTemplate;

    public void save(TicketLock ticketLock) {
        jdbcTemplate.update(SQL_TICKET_LOCK_SAVE, new Object[]{ticketLock.getId(), ticketLock.getCount(), ticketLock.getOrderId(), ticketLock.getCreateDate()});
    }
}
