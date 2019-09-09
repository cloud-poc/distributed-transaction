package org.akj.springboot.tx.configuration;

import com.atomikos.jdbc.AtomikosDataSourceBean;
import com.mysql.cj.jdbc.MysqlXADataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaVendorAdapter;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;

import javax.sql.DataSource;
import java.sql.SQLException;
import java.util.HashMap;

@Configuration
@EnableJpaRepositories(basePackages = "org.akj.springboot.tx.entity.audit", entityManagerFactoryRef =
        "auditEntityManager", transactionManagerRef = "transactionManager")
@DependsOn("transactionManager")
public class AuditDataSourceConfiguration {

    @Autowired
    private JpaVendorAdapter jpaVendorAdapter;

    @Bean
    @ConfigurationProperties(prefix = "spring.datasource.audit")
    public org.springframework.boot.autoconfigure.jdbc.DataSourceProperties auditDataSourceProperties() {
        return new org.springframework.boot.autoconfigure.jdbc.DataSourceProperties();
    }

    @Bean(name = "auditDataSource")
    public DataSource auditDataSource() throws SQLException {
        MysqlXADataSource mysqlXaDataSource = new MysqlXADataSource();
        mysqlXaDataSource.setURL(auditDataSourceProperties().getUrl());
        mysqlXaDataSource.setUser(auditDataSourceProperties().getUsername());
        mysqlXaDataSource.setPassword(auditDataSourceProperties().getPassword());
        mysqlXaDataSource.setPinGlobalTxToPhysicalConnection(true);

        AtomikosDataSourceBean xaDataSource = new AtomikosDataSourceBean();
        xaDataSource.setXaDataSource(mysqlXaDataSource);
        xaDataSource.setUniqueResourceName("auditDataSource");
        xaDataSource.setMaxPoolSize(100);
        xaDataSource.setMinPoolSize(20);
        return xaDataSource;
    }

    @Bean(name = "auditEntityManager")
    @DependsOn("transactionManager")
    public LocalContainerEntityManagerFactoryBean auditEntityManager() throws Throwable {
        HashMap<String, Object> properties = new HashMap<String, Object>();
        properties.put("hibernate.transaction.jta.platform", AtomikosJtaPlatform.class.getName());
        properties.put("javax.persistence.transactionType", "JTA");

        LocalContainerEntityManagerFactoryBean entityManager = new LocalContainerEntityManagerFactoryBean();
        entityManager.setJtaDataSource(auditDataSource());
        entityManager.setJpaVendorAdapter(jpaVendorAdapter);
        entityManager.setPackagesToScan("org.akj.springboot.tx.entity.audit");
        entityManager.setPersistenceUnitName("auditPersistenceUnit");
        entityManager.setJpaPropertyMap(properties);

        return entityManager;
    }
}
