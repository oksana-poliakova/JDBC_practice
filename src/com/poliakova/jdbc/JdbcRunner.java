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

        String insertedValues = """
                INSERT INTO info (data)
                VALUES 
                ('Test1'),
                ('Test2'),
                ('Test3'),
                ('Test4');
                """;

        String testValue = """
                UPDATE info
                SET data = 'Test5'
                WHERE id = 5;
                """;

        try (var connection = ConnectionManager.getConnection();
             var statement = connection.createStatement()) {
            // Print the transaction isolation level of the connection
            System.out.println(connection.getTransactionIsolation());

            // Print the schema of the connection
            System.out.println(connection.getSchema());

            // Execute the SQL statements
            var executeResult = statement.executeUpdate(sql);
            System.out.println(executeResult);

            var executeResult1 = statement.executeUpdate(insertedValues);
            System.out.println(insertedValues);

            var executeResult2 = statement.executeUpdate(testValue);
            System.out.println(executeResult2);

        }
    }
}
