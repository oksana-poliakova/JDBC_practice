package com.poliakova.jdbc;

import com.poliakova.jdbc.Dao.TeachersDao;
import com.poliakova.jdbc.entity.Teachers;

/**
 * @author Oksana Poliakova on 12.07.2023
 * @projectName JDBC_practice
 */
public class DaoRunner {
    public static void main(String[] args) {
//        saveTest();
//        deleteTest();
//        findByIdTest();
        findAllTeachers();
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

    // Test method for selecting a teacher by id
    private static void findByIdTest() {
        var teacherDao = TeachersDao.getInstance();
        var maybeTeacher = teacherDao.findById(1);
        System.out.println(maybeTeacher);

        maybeTeacher.ifPresent(teachers -> {
            teachers.setTeacherName("Updated name for Hans Müller");
            teacherDao.update(teachers);
        });
    }

    // Test method for selecting all teachers
    private static void findAllTeachers() {
        var teachers = TeachersDao.getInstance().findAllTeachers();
        System.out.println(teachers);
    }
}
