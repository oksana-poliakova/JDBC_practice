package com.poliakova.jdbc;

import com.poliakova.jdbc.Dao.TeachersDao;
import com.poliakova.jdbc.entity.Teachers;

/**
 * @author Oksana Poliakova on 12.07.2023
 * @projectName JDBC_practice
 */
public class DaoRunner {
    public static void main(String[] args) {
        saveTest();
//        deleteTest();
    }

    // Test method for deleting a teacher
    private static void deleteTest() {
        var teacherDao = TeachersDao.getInstance();
        boolean deleteResult = teacherDao.delete(20);

        System.out.println(deleteResult);
    }

    // Test method for saving a teacher
    private static void saveTest() {
        var teacherDao = TeachersDao.getInstance();
        var teacher = new Teachers();
        teacher.setTeacherId(20);
        teacher.setTeacherName("Test Name");
        teacher.setTeacherEmail("Test Email");

        var savedTeacher = teacherDao.save(teacher);
        System.out.println(savedTeacher);
    }
}
