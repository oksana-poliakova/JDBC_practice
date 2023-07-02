package com.poliakova.jdbc.util;

import org.postgresql.Driver;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * @author Oksana Poliakova on 02.07.2023
 * @projectName JDBC_practice
 */
public final class ConnectionManager {
    private static final String USERNAME = "postgres";
    private static final String PASSWORD = "2016";
    private static final String URL = "jdbc:postgresql://localhost:5432/university_course";

    // Load driver
    private static void loadDriver() {
        try {
            Class.forName("org.postgresql.Driver");
        } catch (ClassNotFoundException error) {
            throw new RuntimeException(error);
        }
    }

    private ConnectionManager() { }

    public static Connection getConnection() {
        try {
            return DriverManager.getConnection(URL, USERNAME, PASSWORD);
        } catch (SQLException error) {
            throw new RuntimeException(error);
        }
    }
}
