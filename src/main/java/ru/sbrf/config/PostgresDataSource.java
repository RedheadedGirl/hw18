package ru.sbrf.config;

import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.stereotype.Component;

@Component
public class PostgresDataSource extends DriverManagerDataSource {

    public PostgresDataSource() {
        this.setUrl("jdbc:postgresql://localhost:5432/recipes");
        this.setUsername("postgres");
        this.setPassword("postgres");
    }
}