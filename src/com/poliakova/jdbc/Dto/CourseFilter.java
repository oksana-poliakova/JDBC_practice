package com.poliakova.jdbc.Dto;

/**
 * @author Oksana Poliakova on 12.07.2023
 * @projectName JDBC_practice
 */
public record CourseFilter(int limit,
                           int offset,
                           String courseName,
                           String courseDescription) {
}
