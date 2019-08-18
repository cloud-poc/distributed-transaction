package org.akj.springboot.tx;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.jms.support.converter.MappingJackson2MessageConverter;
import org.springframework.jms.support.converter.MessageConverter;

@SpringBootApplication
public class Application {

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

    //@Bean
    public MessageConverter messageConverter() {
        return new MappingJackson2MessageConverter();
    }

    @Bean
    public ActiveMQConnectionFactory connectionFactory() {
        // use default broker url
        ActiveMQConnectionFactory connectionFactory = new ActiveMQConnectionFactory();
        connectionFactory.setBrokerURL("tcp://localhost:61616");
        connectionFactory.setMaxThreadPoolSize(20);
        connectionFactory.setSendTimeout(10000);
        connectionFactory.setSendAcksAsync(true);
        connectionFactory.setUseAsyncSend(true);
        // javax.jms.JMSException: Failed to build body from content. Serializable class not available to broker
        connectionFactory.setTrustAllPackages(true);

        return connectionFactory;
    }
}
