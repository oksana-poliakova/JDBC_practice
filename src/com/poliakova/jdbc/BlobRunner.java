package com.poliakova.jdbc;

import com.poliakova.jdbc.util.ConnectionManager;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * @author Oksana Poliakova on 12.07.2023
 * @projectName JDBC_practice
 */
public class BlobRunner {
    public static void main(String[] args) throws SQLException, IOException {
        saveImage();
        getImage();
    }

    // Getting image
    private static void getImage() throws SQLException, IOException {
        var sql = """
                SELECT students_image
                FROM students
                WHERE student_id = ?
                """;
        try (var connection = ConnectionManager.get();
             var preraredStatement = connection.prepareStatement(sql)) {
            preraredStatement.setInt(1, 3);
            var resultSet = preraredStatement.executeQuery();
            if (resultSet.next()) {
                var image = resultSet.getBytes("students_image");
                Files.write(Path.of("resources", "picture1_updated.png"), image, StandardOpenOption.CREATE);
            }
        }
    }

    // Storage image
    private static void saveImage() throws SQLException, IOException {
        var sql = """
                UPDATE students
                SET students_image = ?
                WHERE student_id = 3
                """;

        try (var connection = ConnectionManager.get();
             var preraredStatement = connection.prepareStatement(sql)) {
            preraredStatement.setBytes(1, Files.readAllBytes(Path.of("resources", "picture1.png")));
            preraredStatement.executeUpdate();
        }
    }
}
