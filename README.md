# 分布式事务
### spring-tx-jpa(monolithic app)
spring jpa transaction
```$xslt
DefaultTransactionDefinition definition = new DefaultTransactionDefinition();
definition.setIsolationLevel(TransactionDefinition.ISOLATION_REPEATABLE_READ);
definition.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRED);
TransactionStatus transactionStatus = transactionManager.getTransaction(definition);

try {
    customer = repository.save(customer);
    simulateException();
    transactionManager.commit(transactionStatus);
} catch (Exception e) {
    transactionManager.rollback(transactionStatus);
    throw e;
}
```

### spring-tx-jta (monolithic app)
spring JTA 事务，采用atomikos来做jta事务管理支持
```$xslt
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-jta-atomikos</artifactId>
</dependency>
```

### spring-dtx-db (multiple datasource)
use ChainedTransactionManager  
case 1: database + database

### spring-dtx-jms-db
use TransactionAwareConnectionFactoryProxy  
case 2：activemq + database

### 简单售票系统
- api-gateway  zuul网关
- hystrix-dashboard  hystrix流量监控大盘
- order-model  order dto
- order-service  订单服务
- ticket-service 票务服务
- customer-service 简单用户服务和付款结算服务

分布式方案：消息队列 + ChainedTransactionManager + TransactionAwareConnectionFactoryProxy
消息重试 + 幂等保证

