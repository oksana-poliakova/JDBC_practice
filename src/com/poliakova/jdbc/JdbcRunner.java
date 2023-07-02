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
        // SQL query to create a table if it doesn't exist
        String sql = """
                CREATE TABLE IF NOT EXISTS info (
                 id SERIAL PRIMARY KEY,
                 data TEXT NOT NULL
                );
                 """;
        try (var connection = ConnectionManager.getConnection();
             var statement = connection.createStatement()) {
            // Print the transaction isolation level of the connection
            System.out.println(connection.getTransactionIsolation());

            // Print the schema of the connection
            System.out.println(connection.getSchema());

            // Execute the SQL statement
            var executeResult = statement.execute(sql);
            System.out.println(executeResult);
        }
    }
}
