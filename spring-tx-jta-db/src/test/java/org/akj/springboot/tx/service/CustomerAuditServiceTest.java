package org.akj.springboot.tx.service;

import org.akj.springboot.tx.dto.CustomerAuditDto;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

@SpringBootTest
class CustomerAuditServiceTest {
    @Autowired
    private CustomerAuditService service;
    private int pageNo = 0;
    private Date date;

    @BeforeEach
    public void before() throws ParseException {
        date = new SimpleDateFormat("yyyy-MM-dd").parse("2019-8-29");
    }

    @Test
    void findCustomerAuditLog() {
        CustomerAuditDto dto = service.findCustomerAuditLog(date, pageNo);

        Assertions.assertTrue(dto.getTotalRecords() > 0);
        Assertions.assertEquals(dto.getTotalRecords(), dto.getAuditLoglist().size());
    }
}