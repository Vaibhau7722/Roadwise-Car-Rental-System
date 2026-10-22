package com.carrental.util;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import java.sql.Connection;
import java.sql.SQLException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class Database {
    private static final Logger log=LoggerFactory.getLogger(Database.class);
    private static volatile HikariDataSource source;
    private Database() {}
    public static Connection getConnection() throws SQLException { return dataSource().getConnection(); }
    public static synchronized HikariDataSource dataSource() {
        if(source!=null && !source.isClosed()) return source;
        String url=System.getenv("DB_URL"), user=System.getenv("DB_USERNAME"), password=System.getenv("DB_PASSWORD");
        if(url==null || url.isBlank() || user==null || user.isBlank()) {
            String message="Database configuration missing. Set DB_URL, DB_USERNAME, and DB_PASSWORD before using the application.";
            log.error(message);
            throw new IllegalStateException(message);
        }
        HikariConfig cfg=new HikariConfig(); cfg.setJdbcUrl(url); cfg.setUsername(user); cfg.setPassword(password==null?"":password);
        cfg.setDriverClassName("com.mysql.cj.jdbc.Driver"); cfg.setMaximumPoolSize(10); cfg.setMinimumIdle(1);
        cfg.setPoolName("CarRentalPool"); cfg.setConnectionTimeout(10000); cfg.setValidationTimeout(5000);
        source=new HikariDataSource(cfg); return source;
    }
    public static synchronized void close() { if(source!=null && !source.isClosed()) source.close(); source=null; }
}
