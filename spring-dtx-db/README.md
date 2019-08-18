Spring based Distributed transaction management
### case 1 
transaction management across two database(RDS)  
#### jdbc + jdbc
- datasource config file
```$xslt
spring.datasource:
  order:
    driver-class-name: com.mysql.cj.jdbc.Driver
    url: jdbc:mysql://localhost:3306/xa?allowPublicKeyRetrieval=true&useSSL=false&charset=utf8
    username: jamie
    password: Hsbc1234
  user:
    driver-class-name: com.mysql.cj.jdbc.Driver
    url: jdbc:mysql://maria.c8rp1x5gocqr.ap-northeast-1.rds.amazonaws.com:3306/user?allowPublicKeyRetrieval=true&useSSL=false&charset=utf8
    username: xxx
    password: Hsbc1234
```
- datasource config bean
```$xslt
@Configuration
public class DatasourceConfiguration {
    @Bean
    @ConfigurationProperties(prefix = "spring.datasource.user")
    public DataSourceProperties userDataSourceProperties() {
        return new DataSourceProperties();
    }

    @Bean
    public DataSource userDataSource() {
        return userDataSourceProperties().initializeDataSourceBuilder().type(HikariDataSource.class).build();
    }

    @Bean
    public JdbcTemplate userJdbcTemplate(@Qualifier("userDataSource") DataSource userDataSource) {
        return new JdbcTemplate(userDataSource);
    }

    @Bean
    @Primary
    @ConfigurationProperties(prefix = "spring.datasource.order")
    public DataSourceProperties orderDataSourceProperties() {
        return new DataSourceProperties();
    }

    @Bean
    @Primary
    public DataSource orderDataSource() {
        return orderDataSourceProperties().initializeDataSourceBuilder().type(HikariDataSource.class).build();
    }

    @Bean
    @Primary
    public JdbcTemplate orderJdbcTemplate(@Qualifier("orderDataSource") DataSource orderDataSource) {
        return new JdbcTemplate(orderDataSource);
    }

    @Bean
    public PlatformTransactionManager platformTransactionManager() {
        DataSourceTransactionManager userTransactionManager = new DataSourceTransactionManager(userDataSource());
        DataSourceTransactionManager orderTransactionManager = new DataSourceTransactionManager(orderDataSource());
        ChainedTransactionManager chainedTransactionManager = new ChainedTransactionManager(orderTransactionManager, userTransactionManager);

        return chainedTransactionManager;
    }
}
```

#### jpa + jdbc
