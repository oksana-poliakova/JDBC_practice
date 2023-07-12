package com.poliakova.jdbc.Dao;

import com.poliakova.jdbc.Dto.TeacherFilter;
import com.poliakova.jdbc.Exception.DaoException;
import com.poliakova.jdbc.entity.Teachers;
import com.poliakova.jdbc.util.ConnectionManager;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

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

    // UPDATE
    public static final String UPDATE_SQL = """
            UPDATE teachers
            SET
                teacher_name = ?,
                teacher_email = ?
            WHERE teacher_id = ?;
            """;

    // Update a teacher
    public void update(Teachers teachers) {
        try (var connection = ConnectionManager.get();
             var preparedStatement = connection.prepareStatement(UPDATE_SQL)) {
            preparedStatement.setString(1, teachers.getTeacherName());
            preparedStatement.setString(2, teachers.getTeacherEmail());
            preparedStatement.setInt(3, teachers.getTeacherId());

            preparedStatement.executeUpdate();
        } catch (SQLException throwables) {
            throw new DaoException(throwables);
        }
    }

    // SELECT

    // SELECT ALL TEACHERS
    public static final String FIND_ALL_SQL = """
            SELECT  teacher_id, 
                    teacher_name, 
                    teacher_email
            FROM teachers
            """;

    public static final String FIND_BY_ID_SQL = FIND_ALL_SQL + """
          
            WHERE teacher_id = ?
            """;

    // Select all teachers
    public List<Teachers> findAllTeachers() {
        try (var connection = ConnectionManager.get();
             var preparedStatement = connection.prepareStatement(FIND_ALL_SQL)) {
            var resultSet = preparedStatement.executeQuery();
            List<Teachers> teachers = new ArrayList<>();

            // Iterate over the result set and build Teachers objects
            while (resultSet.next()) {
                teachers.add(buildTeacher(resultSet));
            }
            // Return the list of Teachers objects
            return teachers;
        } catch (SQLException throwables) {
            throw new DaoException(throwables);
        }
    }

    // Select all teachers with filter
    public List<Teachers> findAllTeachers(TeacherFilter filter) {
        // Create lists to store parameters and WHERE conditions
        List<Object> parameters = new ArrayList<>();
        List<String> whereSql = new ArrayList<>();

        // Check if teacherName filter is specified
        if (filter.teacherName() != null) {
            whereSql.add("teacher_name LIKE ?");
            parameters.add(filter.teacherName());
        }

        // Check if teacherEmail filter is specified
        if (filter.teacherEmail() != null) {
            whereSql.add("teacher_email = ?");
            parameters.add("%" + filter.teacherEmail() + "%");
        }

        // Add limit and offset parameters
        parameters.add(filter.limit());
        parameters.add(filter.offset());

        // Generate the WHERE clause based on the filter conditions
        var where = whereSql.stream()
                .collect(Collectors.joining(" AND ", " WHERE ", " LIMIT ? OFFSET ? "));

        // Combine the WHERE clause with the base SQL query
        var sql = FIND_ALL_SQL + where;

        try (var connection = ConnectionManager.get();
             var preparedStatement = connection.prepareStatement(sql)) {
            for (int i = 0; i < parameters.size(); i++) {
                preparedStatement.setObject(i + 1, parameters.get(i));
            }

            // Execute the query and process the result set
            final ResultSet resultSet = preparedStatement.executeQuery();
            List<Teachers> teachers = new ArrayList<>();
            while (resultSet.next()) {
                teachers.add(buildTeacher(resultSet));
            }
            System.out.println(preparedStatement);

            // Return the list of Teachers objects
            return teachers;
        } catch (SQLException throwables) {
            throw new DaoException(throwables);
        }
    }

    // Select a teacher by id
    public Optional<Teachers> findById(Integer teachersId) {
        try (var connection = ConnectionManager.get();
             var preparedStatement = connection.prepareStatement(FIND_BY_ID_SQL)) {
            preparedStatement.setInt(1, teachersId);

            var resultSet = preparedStatement.executeQuery();
            Teachers teachers = null;

            // Check if a teacher was found in the result set
            if (resultSet.next()) {
                teachers = buildTeacher(resultSet);
            }
            // Return an Optional object containing the Teachers object (or null if not found)
            return Optional.ofNullable(teachers);
        } catch (SQLException throwables) {
            throw new DaoException(throwables);
        }
    }

    // Build teacher
    private static Teachers buildTeacher(ResultSet resultSet) throws SQLException {
        return new Teachers(
                resultSet.getInt("teacher_id"),
                resultSet.getString("teacher_name"),
                resultSet.getString("teacher_email")
        );
    }

    // Get the singleton instance
    public static TeachersDao getInstance() {
        return INSTANCE;
    }
}
