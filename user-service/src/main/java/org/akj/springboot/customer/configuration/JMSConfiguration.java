package org.akj.springboot.customer.configuration;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.activemq.RedeliveryPolicy;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jms.connection.TransactionAwareConnectionFactoryProxy;
import org.springframework.jms.core.JmsTemplate;

import javax.jms.ConnectionFactory;

@Configuration
public class JMSConfiguration {

    @Bean
    public ConnectionFactory connectionFactory(){
        ActiveMQConnectionFactory factory = new ActiveMQConnectionFactory("tcp://localhost:61616");
        factory.setTrustAllPackages(true);
        //factory.setMaxThreadPoolSize(5);

        RedeliveryPolicy queuePolicy = new RedeliveryPolicy();
        queuePolicy.setInitialRedeliveryDelay(0);
        queuePolicy.setRedeliveryDelay(1000);
        queuePolicy.setUseExponentialBackOff(false);
        queuePolicy.setMaximumRedeliveries(2);
        factory.setRedeliveryPolicy(queuePolicy);

        TransactionAwareConnectionFactoryProxy factoryProxy = new TransactionAwareConnectionFactoryProxy();
        factoryProxy.setSynchedLocalTransactionAllowed(true);
        factoryProxy.setTargetConnectionFactory(factory);

        return factoryProxy;
    }

    //@Bean
    public JmsTemplate jmsTemplate(ConnectionFactory connectionFactory){
        JmsTemplate jmsTemplate = new JmsTemplate(connectionFactory);
        jmsTemplate.setSessionTransacted(true);

        return jmsTemplate;
    }
}
