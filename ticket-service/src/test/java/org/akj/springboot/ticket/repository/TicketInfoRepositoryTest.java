package org.akj.springboot.ticket.repository;

import com.fasterxml.uuid.Generators;
import com.fasterxml.uuid.impl.TimeBasedGenerator;
import org.akj.springboot.ticket.entity.TicketInfo;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;

@SpringBootTest
class TicketInfoRepositoryTest {
    @Autowired
    private TicketInfoRepository repository;

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

       repository.save(ticketInfo);
    }

}