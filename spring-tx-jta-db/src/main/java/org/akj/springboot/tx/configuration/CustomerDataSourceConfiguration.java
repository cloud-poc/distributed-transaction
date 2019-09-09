package org.akj.springboot.tx.configuration;

import com.atomikos.jdbc.AtomikosDataSourceBean;
import com.mysql.cj.jdbc.MysqlXADataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;
import org.springframework.context.annotation.Primary;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaVendorAdapter;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;

import javax.sql.DataSource;
import java.sql.SQLException;
import java.util.HashMap;

@Configuration
@EnableJpaRepositories(basePackages = "org.akj.springboot.tx.entity.customer", entityManagerFactoryRef =
        "customerEntityManager", transactionManagerRef = "transactionManager")
@DependsOn("transactionManager")
public class CustomerDataSourceConfiguration {
    @Autowired
    private JpaVendorAdapter jpaVendorAdapter;

    @Bean
    @Primary
    @ConfigurationProperties(prefix = "spring.datasource.user")
    public org.springframework.boot.autoconfigure.jdbc.DataSourceProperties customerDataSourceProperties() {
        return new org.springframework.boot.autoconfigure.jdbc.DataSourceProperties();
    }

    @Bean(name = "customerDataSource")
    @Primary
    public DataSource customerDataSource() throws SQLException {
        MysqlXADataSource mysqlXaDataSource = new MysqlXADataSource();
        mysqlXaDataSource.setURL(customerDataSourceProperties().getUrl());
        mysqlXaDataSource.setUser(customerDataSourceProperties().getUsername());
        mysqlXaDataSource.setPassword(customerDataSourceProperties().getPassword());
        mysqlXaDataSource.setPinGlobalTxToPhysicalConnection(true);

        AtomikosDataSourceBean xaDataSource = new AtomikosDataSourceBean();
        xaDataSource.setXaDataSource(mysqlXaDataSource);
        xaDataSource.setUniqueResourceName("customerDataSource");
        xaDataSource.setMaxPoolSize(100);
        xaDataSource.setMinPoolSize(20);
        return xaDataSource;
    }

    @Bean(name = "customerEntityManager")
    @DependsOn("transactionManager")
    @Primary
    public LocalContainerEntityManagerFactoryBean customerEntityManager() throws Throwable {
        HashMap<String, Object> properties = new HashMap<String, Object>();
        properties.put("hibernate.transaction.jta.platform", AtomikosJtaPlatform.class.getName());
        properties.put("javax.persistence.transactionType", "JTA");

        LocalContainerEntityManagerFactoryBean entityManager = new LocalContainerEntityManagerFactoryBean();
        entityManager.setJtaDataSource(customerDataSource());
        entityManager.setJpaVendorAdapter(jpaVendorAdapter);
        entityManager.setPackagesToScan("org.akj.springboot.tx.entity.customer");
        entityManager.setPersistenceUnitName("customerPersistenceUnit");
        entityManager.setJpaPropertyMap(properties);

        return entityManager;
    }

}
