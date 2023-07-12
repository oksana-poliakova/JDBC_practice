package com.poliakova.jdbc.Dao;

import com.poliakova.jdbc.Exception.DaoException;
import com.poliakova.jdbc.entity.Teachers;
import com.poliakova.jdbc.util.ConnectionManager;

import java.sql.SQLException;
import java.sql.Statement;

/**
 * @author Oksana Poliakova on 12.07.2023
 * @projectName JDBC_practice
 */

public class TeachersDao {
    // Singleton instance
    private static final TeachersDao INSTANCE = new TeachersDao();

    // DELETE
    private static final String DELETE_SQL = """
            DELETE FROM teachers
            WHERE teacher_id = ?
            """;

    // Delete a teacher by ID
    public boolean delete(Integer teacherId) {
        try (var connection = ConnectionManager.get();
        var preparedStatement = connection.prepareStatement(DELETE_SQL)) {
            preparedStatement.setInt(1, teacherId);

            return preparedStatement.executeUpdate() > 0;
        } catch (SQLException throwables) {
            throw new DaoException(throwables);
        }
    }

    // CREATE
    public static final String SAVE_SQL = """
            INSERT INTO teachers (teacher_id, teacher_name, teacher_email)
            VALUES (?, ?, ?);
            """;

    // Save a teacher
    public Teachers save(Teachers teachers) {
        try (var connection = ConnectionManager.get();
        var preparedStatement = connection.prepareStatement(SAVE_SQL, Statement.RETURN_GENERATED_KEYS)) {
            preparedStatement.setInt(1, teachers.getTeacherId());
            preparedStatement.setString(2, teachers.getTeacherName());
            preparedStatement.setString(3, teachers.getTeacherEmail());

            preparedStatement.executeUpdate();

            var generatedKeys = preparedStatement.getGeneratedKeys();
            if (generatedKeys.next()) {
                teachers.setTeacherId(generatedKeys.getInt("teacher_id"));
            }
            return teachers;
        } catch (SQLException throwables) {
            throw new DaoException(throwables);
        }
    }

    // Get the singleton instance
    public static TeachersDao getInstance() {
        return INSTANCE;
    }
}
