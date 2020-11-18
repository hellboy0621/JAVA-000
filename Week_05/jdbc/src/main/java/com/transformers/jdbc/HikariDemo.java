package com.transformers.jdbc;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import java.sql.Connection;
import java.sql.SQLException;

public class HikariDemo {
    public static void main(String[] args) {
        HikariConfig config = new HikariConfig();
        config.setJdbcUrl(Constant.URL);
        config.setUsername(Constant.USER_NAME);
        config.setPassword(Constant.PASSWORD);
        config.addDataSourceProperty("cachePrepStmts", "true");
        config.addDataSourceProperty("prepStmtCacheSize", "250");
        config.addDataSourceProperty("prepStmtCacheSqlLimit", "2048");

        HikariDataSource ds = new HikariDataSource(config);
        try {
            Connection conn = ds.getConnection();
            JdbcDemo.query(conn);
            System.out.println("*************");
            JdbcDemo.insert(conn);
            JdbcDemo.query(conn);
            System.out.println("*************");
            JdbcDemo.update(conn);
            JdbcDemo.query(conn);
            System.out.println("*************");
            JdbcDemo.delete(conn);
            JdbcDemo.query(conn);
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            ds.close();
        }

    }
}
