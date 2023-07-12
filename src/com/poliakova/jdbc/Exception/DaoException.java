package com.poliakova.jdbc.Exception;

/**
 * @author Oksana Poliakova on 12.07.2023
 * @projectName JDBC_practice
 */
public class DaoException extends RuntimeException {
    public DaoException(Throwable throwable) {
        super(throwable);
    }
}
