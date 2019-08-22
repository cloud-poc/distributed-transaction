package org.akj.springboot.order.configuration;

import com.zaxxer.hikari.HikariDataSource;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.transaction.ChainedTransactionManager;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;

import javax.sql.DataSource;

@Configuration
public class DatasourceConfiguration {
    @Bean
    @ConfigurationProperties(prefix = "spring.datasource.ticket")
    public DataSourceProperties ticketDataSourceProperties() {
        return new DataSourceProperties();
    }

    @Bean
    public DataSource ticketDataSource() {
        return ticketDataSourceProperties().initializeDataSourceBuilder().type(HikariDataSource.class).build();
    }

    @Bean
    public JdbcTemplate ticketJdbcTemplate(@Qualifier("ticketDataSource") DataSource ticketDataSource) {
        return new JdbcTemplate(ticketDataSource);
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

    @Bean
    public LocalContainerEntityManagerFactoryBean entityManagerFactory() {
        HibernateJpaVendorAdapter vendorAdapter = new HibernateJpaVendorAdapter();
        vendorAdapter.setDatabasePlatform("org.hibernate.dialect.MySQL8Dialect");
        vendorAdapter.setGenerateDdl(true);
        vendorAdapter.setShowSql(true);

        LocalContainerEntityManagerFactoryBean factoryBean = new LocalContainerEntityManagerFactoryBean();
        factoryBean.setDataSource(orderDataSource());
        factoryBean.setPackagesToScan("org.akj.springboot");
        factoryBean.setJpaVendorAdapter(vendorAdapter);
        return factoryBean;
    }

    @Bean
    public PlatformTransactionManager transactionManager() {
        DataSourceTransactionManager ticketTransactionManager = new DataSourceTransactionManager(ticketDataSource());
        JpaTransactionManager jpaTransactionManager = new JpaTransactionManager();
        jpaTransactionManager.setEntityManagerFactory(entityManagerFactory().getObject());

        ChainedTransactionManager chainedTransactionManager = new ChainedTransactionManager(ticketTransactionManager,jpaTransactionManager);

        return chainedTransactionManager;
    }
}
