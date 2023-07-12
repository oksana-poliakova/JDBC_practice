package com.poliakova.jdbc.Dao;

import com.poliakova.jdbc.Exception.DaoException;
import com.poliakova.jdbc.entity.Course;
import com.poliakova.jdbc.util.ConnectionManager;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * @author Oksana Poliakova on 12.07.2023
 * @projectName JDBC_practice
 */
public class CourseDao {
    private static final CourseDao INSTANCE = new CourseDao();

    // SQL query to retrieve all courses with JOIN
    private static final String FIND_ALL_SQL = """
            SELECT c.course_id,
                   c.course_name,
                   c.course_description
            FROM course c
            JOIN enrollments e ON c.course_id = e.course_id
            """;

    // SQL query to retrieve a course by ID with JOIN
    private static final String FIND_BY_ID_SQL = FIND_ALL_SQL + """
            WHERE c.course_id = ?
            """;

    // Retrieve all courses
    public List<Course> findAllCourses() {
        try (var connection = ConnectionManager.get();
             var preparedStatement = connection.prepareStatement(FIND_ALL_SQL)) {
            var resultSet = preparedStatement.executeQuery();
            List<Course> courses = new ArrayList<>();

            // Iterate over the result set and build Course objects
            while (resultSet.next()) {
                courses.add(buildCourse(resultSet));
            }

            // Return the list of Course objects
            return courses;
        } catch (SQLException throwables) {
            throw new DaoException(throwables);
        }
    }

    // Retrieve a course by ID
    public Optional<Course> findCourseById(Integer courseId) {
        try (var connection = ConnectionManager.get();
             var preparedStatement = connection.prepareStatement(FIND_BY_ID_SQL)) {
            preparedStatement.setInt(1, courseId);
            var resultSet = preparedStatement.executeQuery();

            // Check if a course was found in the result set
            if (resultSet.next()) {
                // Build a Course object from the result set
                return Optional.of(buildCourse(resultSet));
            } else {
                // Return an empty Optional if course is not found
                return Optional.empty();
            }
        } catch (SQLException throwables) {
            throw new DaoException(throwables);
        }
    }

    // Build a Course object from a result set row
    private Course buildCourse(ResultSet resultSet) throws SQLException {
        Integer courseId = resultSet.getInt("course_id");
        String courseName = resultSet.getString("course_name");
        String courseDescription = resultSet.getString("course_description");

        // Create and return a new Course object
        return new Course(courseId, courseName, courseDescription);
    }
}
