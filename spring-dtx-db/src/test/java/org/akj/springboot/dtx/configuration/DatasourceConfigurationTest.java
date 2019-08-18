package org.akj.springboot.dtx.configuration;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.sql.DataSource;

import static org.junit.jupiter.api.Assertions.*;
@SpringBootTest
class DatasourceConfigurationTest {

    @Autowired
    private DataSource userDataSource;
    @Autowired
    private DataSource orderDataSource;

    @Test
    public void test(){
        assertNotNull(userDataSource);
        assertNotNull(orderDataSource);
    }

}