package com.poliakova.jdbc;

import com.poliakova.jdbc.util.ConnectionManager;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Set;

/**
 * @author Oksana Poliakova on 06.07.2023
 * @projectName JDBC_practice
 */
public class TransactionRunner {
    public static void main(String[] args) throws SQLException {
        int studentId = 2; // Specify the ID of the student to be deleted
        var deleteStudentSql = "DELETE FROM students WHERE id = ?"; // statement to delete a student from the table
        var deleteGradeSql = "DELETE FROM grades WHERE grade_id = ?"; // statement to delete a related from the table

        Connection connection = null; // Initialize the connection variable
        // Initialize the prepared statement for deleting the student and related table
        PreparedStatement deleteStudentStatement = null;
        PreparedStatement deleteGradeStatement = null;
        try {
            connection = ConnectionManager.getConnection(); // Get a database connection
            // Prepare the delete student and grade statements
            deleteStudentStatement = connection.prepareStatement(deleteStudentSql);
            deleteGradeStatement = connection.prepareStatement(deleteGradeSql);

            connection.setAutoCommit(false); // Disable auto-commit to start a transaction

            // Set the student and grade ID parameters for the deleting
            deleteStudentStatement.setLong(1, studentId);
            deleteGradeStatement.setLong(1, studentId);

            deleteGradeStatement.executeUpdate(); // Execute the delete teacher statement
            if (true) {
                throw new RuntimeException("Ooops");
            }
            deleteStudentStatement.executeUpdate(); // Execute the delete student statement
            connection.commit();
        } catch (Exception e) {
            if (connection != null) {
                connection.rollback(); // If any error occurs, rollback the transaction
            }
            throw e; // Rethrow the exception
        } finally {
            // Close the database and statements connections
            if (connection != null) {
                connection.close();
            }
            if (deleteStudentStatement != null) {
                deleteStudentStatement.close();
            }
            if (deleteGradeStatement != null) {
                deleteGradeStatement.close();
            }
        }
    }
}
