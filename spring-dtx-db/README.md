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
- datasource config file - same as 'jdbc+jdbc'
-  datasource config bean
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

    // comment order related config beans, to use JPA instead
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
    /*
    @Bean
    @Primary
    public JdbcTemplate orderJdbcTemplate(@Qualifier("orderDataSource") DataSource orderDataSource) {
        return new JdbcTemplate(orderDataSource);
    }*/

    @Bean
    public LocalContainerEntityManagerFactoryBean entityManagerFactory() {
        HibernateJpaVendorAdapter vendorAdapter = new HibernateJpaVendorAdapter();
        //vendorAdapter.setGenerateDdl(false);
        vendorAdapter.setDatabasePlatform("org.hibernate.dialect.MySQL8Dialect");

        LocalContainerEntityManagerFactoryBean factoryBean = new LocalContainerEntityManagerFactoryBean();
        factoryBean.setDataSource(orderDataSource());
        factoryBean.setPackagesToScan("org.akj.springboot");
        factoryBean.setJpaVendorAdapter(vendorAdapter);

        return factoryBean;
    }

    @Bean
    public PlatformTransactionManager transactionManager() {
        DataSourceTransactionManager userTransactionManager = new DataSourceTransactionManager(userDataSource());
        JpaTransactionManager jpaTransactionManager = new JpaTransactionManager();
        jpaTransactionManager.setEntityManagerFactory(entityManagerFactory().getObject());

        //DataSourceTransactionManager orderTransactionManager = new DataSourceTransactionManager(orderDataSource());
        //ChainedTransactionManager chainedTransactionManager = new ChainedTransactionManager(orderTransactionManager, userTransactionManager);

        ChainedTransactionManager chainedTransactionManager = new ChainedTransactionManager(userTransactionManager,jpaTransactionManager);

        return chainedTransactionManager;
    }
}
```

均是采用了“最大努力一次提交模式 但使用ChainedTransactionManager”模式，该实现方式有潜在隐患:  
如果最后一个commit失败，并不会促使第一提交rollback
