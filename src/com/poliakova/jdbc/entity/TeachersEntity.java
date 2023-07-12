package com.poliakova.jdbc.entity;

/**
 * @author Oksana Poliakova on 12.07.2023
 * @projectName JDBC_practice
 */
public class TeachersEntity {
    private Integer teacherId;
    private String teacherName;
    private String teacherEmail;

    public TeachersEntity(Integer teacherId, String teacherName, String teacherEmail) {
        this.teacherId = teacherId;
        this.teacherName = teacherName;
        this.teacherEmail = teacherEmail;
    }

    public Integer getTeacherId() {
        return teacherId;
    }

    public String getTeacherName() {
        return teacherName;
    }

    public String getTeacherEmail() {
        return teacherEmail;
    }

    public void setTeacherId(Integer teacherId) {
        this.teacherId = teacherId;
    }

    public void setTeacherName(String teacherName) {
        this.teacherName = teacherName;
    }

    public void setTeacherEmail(String teacherEmail) {
        this.teacherEmail = teacherEmail;
    }

    @Override
    public String toString() {
        return "TeachersEntity{" +
               "teacherId=" + teacherId +
               ", teacherName='" + teacherName + '\'' +
               ", teacherEmail='" + teacherEmail + '\'' +
               '}';
    }
}
