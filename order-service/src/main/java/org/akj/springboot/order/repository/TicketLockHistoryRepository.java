package org.akj.springboot.order.repository;

import org.akj.springboot.order.entity.TicketLockHistory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class TicketLockHistoryRepository {

    private static final String SQL_TICKET_LOCK_SAVE = "insert into ticket_lock_history(id,count,order_id,create_date) values (?,?,?,?)";

    @Autowired
    @Qualifier("ticketJdbcTemplate")
    private JdbcTemplate jdbcTemplate;

    public void save(TicketLockHistory ticketLock) {
        jdbcTemplate.update(SQL_TICKET_LOCK_SAVE, new Object[]{ticketLock.getId(), ticketLock.getCount(), ticketLock.getOrderId(), ticketLock.getCreateDate()});
    }
}
