package org.akj.springboot.order.repository;

import com.fasterxml.uuid.Generators;
import com.fasterxml.uuid.impl.TimeBasedGenerator;
import org.akj.springboot.order.model.TicketInfo;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;

import java.time.LocalDateTime;

@SpringBootTest
class TicketInfoRepositoryTest {
    private TicketInfoRepository repository;

    @Autowired
    @Qualifier("ticketJdbcTemplate")
    private JdbcTemplate jdbcTemplate;

    @Test
    //@Transactional
    public void test() {
        TimeBasedGenerator timeBasedGenerator = Generators.timeBasedGenerator();

        TicketInfo ticketInfo = new TicketInfo();
        ticketInfo.setId(timeBasedGenerator.generate().toString());
        ticketInfo.setTotal(5);
        ticketInfo.setName("2019-08-21");
        ticketInfo.setDescription("Ticket for FinTech");
        LocalDateTime now = LocalDateTime.now();
        ticketInfo.setCreateDate(now);
        ticketInfo.setLastUpdateDate(now);

        jdbcTemplate.update("insert into ticket_info(id,name,description,total,create_date,last_update_date) values" +
                "(?,?,?,?,?,?)", new Object[]{ticketInfo.getId(), ticketInfo.getName(), ticketInfo.getDescription(),
                ticketInfo.getTotal(), ticketInfo.getCreateDate(), ticketInfo.getLastUpdateDate()});
    }

}