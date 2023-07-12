package com.poliakova.jdbc.Dto;

/**
 * @author Oksana Poliakova on 12.07.2023
 * @projectName JDBC_practice
 */
public record TeacherFilter(int limit,
                            int offset,
                            String teacherName,
                            String teacherEmail) {

}
