package com.mcit.AdmissionSystem.config;

import com.mchange.v2.c3p0.ComboPooledDataSource;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.beans.PropertyVetoException;


@Configuration
public class DataSourceConfig {

    @Value("${c3p0.max_size}")
    private int maxSize;

    @Value("${c3p0.min_size}")
    private int minSize;

    @Value("${c3p0.acquire_increment}")
    private int acquireIncrement;

    @Value("${c3p0.idle_test_period}")
    private int idleTestPeriod;

    @Value("${c3p0.max_idle_time_excess_connections}")
    private int maxIdleTimeExcessConnections;

    @Value("${c3p0.test_connection_on_checkin}")
    private boolean testConnectionOnCheckin;

    @Value("${c3p0.preferred_test_query}")
    private String preferredTestQuery;

    @Value("${c3p0.url}")
    private String url;

    @Value("${c3p0.username}")
    private String username;

    @Value("${c3p0.password}")
    private String password;

    @Value("${c3p0.driverClassName}")
    private String driverClassName;

   @Bean
    public ComboPooledDataSource dataSource() throws PropertyVetoException {
        ComboPooledDataSource dataSource = new ComboPooledDataSource();
        dataSource.setMaxPoolSize(maxSize);
        dataSource.setMinPoolSize(minSize);
        dataSource.setAcquireIncrement(acquireIncrement);
        dataSource.setIdleConnectionTestPeriod(idleTestPeriod);
        dataSource.setMaxIdleTimeExcessConnections(maxIdleTimeExcessConnections);
        dataSource.setTestConnectionOnCheckin(testConnectionOnCheckin);
        dataSource.setPreferredTestQuery(preferredTestQuery);
        dataSource.setJdbcUrl(url);
        dataSource.setPassword(password);
        dataSource.setUser(username);
        dataSource.setDriverClass(driverClassName);
        return dataSource;
    }
}