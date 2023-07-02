package com.poliakova.jdbc;

import com.poliakova.jdbc.util.ConnectionManager;
import org.postgresql.Driver;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * @author Oksana Poliakova on 02.07.2023
 * @projectName JDBC_practice
 */

public class JdbcRunner {
    public static void main(String[] args) throws SQLException {
        Class<Driver> driverClass = Driver.class;

        try (var connection = ConnectionManager.getConnection()) {
            System.out.println(connection.getTransactionIsolation());
        }
    }
}
