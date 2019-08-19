Spring based Distributed transaction management
### case 2 
transaction management across 1 mq + 1 database
#### activeMQ + jdbc
- config file
```$xslt
spring:
  datasource:
    driver-class-name: com.mysql.cj.jdbc.Driver
    url: jdbc:mysql://localhost:3306/xa?allowPublicKeyRetrieval=true&useSSL=false&charset=utf8
    username: jamie
    password: Hsbc1234
  jpa:
    hibernate:
      ddl-auto: update
    show-sql: true
    database-platform: org.hibernate.dialect.MySQL8Dialect
```
- config beans
```$xslt
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
```

也是一种“最大努力一次提交”模式, 这种模式中有个优势是可以在jms事务没有成功提交前，message broker可以使用重试机制，重新分发message到上次处理的instance或者其他instances

**留意**： 需要注意在该种模式下的消息处理幂等性
  