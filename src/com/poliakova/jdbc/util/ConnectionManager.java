package com.poliakova.jdbc.util;

import org.postgresql.Driver;

import java.lang.reflect.Proxy;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

/**
 * @author Oksana Poliakova on 02.07.2023
 * @projectName JDBC_practice
 */

/**
 * Utility class for managing database connections and connection pool.
 */

public final class ConnectionManager {
    // Keys for retrieving connection information from the properties file
    private static final String USERNAME_KEY = "db.username";
    private static final String PASSWORD_KEY = "db.password";
    private static final String URL_KEY = "db.url";
    private static final String POOL_SIZE_KEY = "db.pool.size";
    private static final Integer DEFAULT_POOL_SIZE = 10;

    // Connection pool (blocking queue)
    private static BlockingQueue<Connection> pool;
    // List of source connections (for closing)
    private static List<Connection> sourceConnections;

    static {
        loadDriver();
        initConnectionPool();
    }

    // Initialize the connection pool
    private static void initConnectionPool() {
        // Get the pool size from the properties file
        var poolSize = PropertiesUtil.get(POOL_SIZE_KEY);
        // If the size is not specified, use the default value
        var size = poolSize == null ? DEFAULT_POOL_SIZE : Integer.parseInt(poolSize);

        // Create a blocking queue with the specified size
        pool = new ArrayBlockingQueue<>(size);
        // Create a list to hold the source connections
        sourceConnections = new ArrayList<>(size);

        // Open and proxy connections in the pool
        for (int i = 0; i < size; i++) {
            // Open a source connection
            var connection = open();
            // Create a proxy object for the connection
            var proxyConnection = (Connection)
                    Proxy.newProxyInstance(ConnectionManager.class.getClassLoader(), new Class[]{Connection.class},
                            (proxy, method, args) -> method.getName().equals("close")
                                    ? pool.add((Connection) proxy) // If the close method is called, return the connection to the pool
                                    : method.invoke(connection, args)); // Otherwise, pass the call to the source connection
            // Add the proxy object to the pool
            pool.add(proxyConnection);
            // Add the source connection to the list
            sourceConnections.add(connection);
        }
    }

    // Open a source connection
    private static Connection open() {
        try {
            return DriverManager.getConnection(
                    PropertiesUtil.get(URL_KEY),
                    PropertiesUtil.get(USERNAME_KEY),
                    PropertiesUtil.get(PASSWORD_KEY)
            );
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    // Get a connection from the pool
    public static Connection get() {
        try {
            return pool.take();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    // Close the connection pool
    public static void closePool() {
        try {
            // Close all the source connections
            for (Connection sourceConnection : sourceConnections) {
                sourceConnection.close();
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    // Load the PostgreSQL driver
    private static void loadDriver() {
        try {
            Class.forName("org.postgresql.Driver");
        } catch (ClassNotFoundException error) {
            throw new RuntimeException(error);
        }
    }

    // Prevent instantiation of the class
    private ConnectionManager() { }
}
