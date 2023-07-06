package com.poliakova.jdbc;

import com.poliakova.jdbc.util.ConnectionManager;

import java.sql.*;

/**
 * @author Oksana Poliakova on 06.07.2023
 * @projectName JDBC_practice
 */
public class DatabaseMetaDataRunner {
    public static void main(String[] args) throws SQLException {
        try {
            checkMetaData();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static void checkMetaData() throws SQLException {
        try (var connection = ConnectionManager.getConnection()) {
            String sql = """
                    SELECT *
                    FROM students
                    """;
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(sql);

            DatabaseMetaData metaData = connection.getMetaData();
            var catalogs = metaData.getCatalogs();

            while (catalogs.next()) {
                System.out.println(catalogs.getString(1));

                var schemas = metaData.getSchemas();
                while (schemas.next()) {
                    System.out.println(schemas.getString(1));
                }
            }
        }
    }
}
