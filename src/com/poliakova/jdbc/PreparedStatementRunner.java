package com.poliakova.jdbc;

import com.poliakova.jdbc.util.ConnectionManager;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * @author Oksana Poliakova on 04.07.2023
 * @projectName JDBC_practice
 */
public class PreparedStatementRunner {
    public static void main(String[] args) {
        testPreparedStatement();
    }

    private static void testPreparedStatement() {
        String sql = """
                SELECT *
                FROM university_course.public.students
                WHERE student_name LIKE ?\s
                 """;

        try (var connection = ConnectionManager.getConnection()) {
            // create prepare statement
            PreparedStatement statement = connection.prepareStatement(sql);

            // set parameters
            statement.setString(1, "E%");

            // execute query
            ResultSet resultSet = statement.executeQuery();

            // result usage
            while (resultSet.next()) {
                String name = resultSet.getString("student_name");
                System.out.println(name);
            }

            resultSet.close();
            statement.close();
            connection.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}

