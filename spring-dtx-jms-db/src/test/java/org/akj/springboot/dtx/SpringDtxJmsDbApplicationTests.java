package org.akj.springboot.dtx;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.jms.core.JmsTemplate;

@SpringBootTest
public class SpringDtxJmsDbApplicationTests {
    @Autowired
    private ApplicationContext context;

    @Test
    public void contextLoads() {
        javax.jms.ConnectionFactory factory = context.getBean(javax.jms.ConnectionFactory.class);
        Assertions.assertNotNull(factory);

        JmsTemplate jmsTemplate = context.getBean(JmsTemplate.class);
        Assertions.assertNotNull(factory);
        System.out.println(jmsTemplate);
    }

}
