package org.example.contactdatabase.core.db;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;


import javax.sql.DataSource;

public class DatabaseHelper {

    private static DatabaseHelper inst;

    public static DatabaseHelper getInstance() {

        return (inst != null) ? inst : (inst = new DatabaseHelper());
    }

    private DataSource dataSource;

    private DatabaseHelper() {
        HikariConfig config = new HikariConfig();
        config.setJdbcUrl("jdbc:postgresql://localhost:5432/contact_db");
        config.setUsername("postgres");
        config.setPassword("");
        config.setDriverClassName("org.postgresql.Driver");
        dataSource = new HikariDataSource(config);
    }

    public DataSource getDataSource() {
        return dataSource;
    }
}
