package com.poliakova.jdbc.Dao;

import com.poliakova.jdbc.Dto.CourseFilter;
import com.poliakova.jdbc.Exception.DaoException;
import com.poliakova.jdbc.entity.Course;
import com.poliakova.jdbc.util.ConnectionManager;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * @author Oksana Poliakova on 12.07.2023
 * @projectName JDBC_practice
 */
public class CourseDao {
    private static final CourseDao INSTANCE = new CourseDao();

    private static final String FIND_ALL_SQL = """
            SELECT c.course_id,
                   c.course_name,
                   c.course_description
            FROM course c
            JOIN enrollments e ON c.course_id = e.course_id
            """;

    private static final String FIND_BY_ID_SQL = FIND_ALL_SQL + """
            WHERE c.course_id = ?
            """;

    public List<Course> findAllCourses() {
        try (var connection = ConnectionManager.get();
             var preparedStatement = connection.prepareStatement(FIND_ALL_SQL)) {
            var resultSet = preparedStatement.executeQuery();
            List<Course> courses = new ArrayList<>();

            while (resultSet.next()) {
                courses.add(buildCourse(resultSet));
            }

            return courses;
        } catch (SQLException throwables) {
            throw new DaoException(throwables);
        }
    }

    public Optional<Course> findCourseById(Integer courseId) {
        try (var connection = ConnectionManager.get();
             var preparedStatement = connection.prepareStatement(FIND_BY_ID_SQL)) {
            preparedStatement.setInt(1, courseId);
            var resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                return Optional.of(buildCourse(resultSet));
            } else {
                return Optional.empty();
            }
        } catch (SQLException throwables) {
            throw new DaoException(throwables);
        }
    }

    public List<Course> findAllCourses(CourseFilter filter) {
        List<Object> parameters = new ArrayList<>();
        List<String> whereSql = new ArrayList<>();

        if (filter.courseName() != null) {
            whereSql.add("course_name LIKE ?");
            parameters.add(filter.courseName());
        }

        if (filter.courseDescription() != null) {
            whereSql.add("course_description LIKE ?");
            parameters.add(filter.courseDescription());
        }

        parameters.add(filter.limit());
        parameters.add(filter.offset());

        var where = whereSql.stream()
                .collect(Collectors.joining(" AND ", " WHERE ", " LIMIT ? OFFSET ? "));

        var sql = FIND_ALL_SQL + where;

        try (var connection = ConnectionManager.get();
             var preparedStatement = connection.prepareStatement(sql)) {
            for (int i = 0; i < parameters.size(); i++) {
                preparedStatement.setObject(i + 1, parameters.get(i));
            }

            final ResultSet resultSet = preparedStatement.executeQuery();
            List<Course> courses = new ArrayList<>();
            while (resultSet.next()) {
                courses.add(buildCourse(resultSet));
            }

            return courses;
        } catch (SQLException throwables) {
            throw new DaoException(throwables);
        }
    }

    private Course buildCourse(ResultSet resultSet) throws SQLException {
        Integer courseId = resultSet.getInt("course_id");
        String courseName = resultSet.getString("course_name");
        String courseDescription = resultSet.getString("course_description");

        return new Course(courseId, courseName, courseDescription);
    }

    public static CourseDao getInstance() {
        return INSTANCE;
    }
}