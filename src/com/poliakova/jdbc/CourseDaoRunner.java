package com.poliakova.jdbc;

import com.poliakova.jdbc.Dao.CourseDao;
import com.poliakova.jdbc.Dao.TeachersDao;
import com.poliakova.jdbc.Dto.CourseFilter;
import com.poliakova.jdbc.Dto.TeacherFilter;
import com.poliakova.jdbc.entity.Course;

import java.util.List;

/**
 * @author Oksana Poliakova on 12.07.2023
 * @projectName JDBC_practice
 */
public class CourseDaoRunner {
    public static void main(String[] args) {
        // Test method to retrieve all courses
//        findAllCoursesTest();

        System.out.println("Next query =====>");

        // Test method to find a course by ID
//        findCourseByIdTest();

        testFindCourseByFilter();
    }

    private static void findAllCoursesTest() {
        // Create a new instance of CourseDao
        var courseDao = new CourseDao();

        // Retrieve all courses using the findAllCourses() method
        var courses = courseDao.findAllCourses();

        // Print the retrieved courses
        for (Course course : courses) {
            System.out.println(course);
        }
    }

    private static void findCourseByIdTest() {
        // Create a new instance of CourseDao
        var courseDao = new CourseDao();

        // Specify the course ID to search for
        var courseId = 1; // Replace with the desired course ID

        // Find a course by its ID using the findCourseById() method
        var optionalCourse = courseDao.findCourseById(courseId);

        // Check if the course was found and print the result
        optionalCourse.ifPresentOrElse(
                course -> System.out.println("Course found: " + course),
                () -> System.out.println("Course not found.")
        );
    }

    // Find course by filter
    private static void testFindCourseByFilter() {
        CourseFilter filter = new CourseFilter(10, 0, "JavaScript", null);
        List<Course> courses = CourseDao.getInstance().findAllCourses(filter);

        if (courses.isEmpty()) {
            System.out.println("No courses found.");
        } else {
            System.out.println("Courses found:");
            for (Course course : courses) {
                System.out.println(course);
            }
        }
    }
}
