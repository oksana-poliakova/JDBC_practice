package com.poliakova.jdbc;

import com.poliakova.jdbc.util.ConnectionManager;
import org.postgresql.Driver;

import java.sql.SQLException;

/**
 * @author Oksana Poliakova on 02.07.2023
 * @projectName JDBC_practice
 */
public class JdbcSelectRunner {
    public static void main(String[] args) throws SQLException {
        String sql = """
                SELECT *
                FROM university_course.public.students
                 """;

        try (var connection = ConnectionManager.getConnection();
             var statement = connection.createStatement()) {

            var executeResult = statement.executeQuery(sql);
            System.out.println(executeResult);

            while (executeResult.next()) {
                int id = executeResult.getInt("student_id");
                String name = executeResult.getString("student_name");
                System.out.println("ID: " + id + ", Name: " + name);
            }
        }
    }
}



