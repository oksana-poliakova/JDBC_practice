package com.poliakova.jdbc.Dao;

/**
 * @author Oksana Poliakova on 12.07.2023
 * @projectName JDBC_practice
 */

public class TeachersDao {

    private static final TeachersDao INSTANCE = new TeachersDao();

    private static TeachersDao getInstance() {
        return INSTANCE;
    }

}
