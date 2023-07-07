package com.poliakova.jdbc;

import com.poliakova.jdbc.util.ConnectionManager;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 * @author Oksana Poliakova on 07.07.2023
 * @projectName JDBC_practice
 */

/**
 * his code uses batch queries to delete multiple students at once.
 * The queries are added to the batch using the addBatch() method,
 * and then executed with a single call to executeBatch().
 * This improves performance and allows multiple delete operations
 * to be performed within a single transaction.
 * */
public class BatchQueryRunner {
    public static void main(String[] args) throws SQLException {
        testBatchQueries();
    }

    private static void testBatchQueries() throws SQLException {
        int[] studentIds = {1, 2, 4}; // Student IDs for deletion
        String deleteStudentSql = "DELETE FROM students WHERE student_id = ?";
        Connection connection = null;
        PreparedStatement deleteStudentStatement = null;

        try {
            connection = ConnectionManager.getConnection();
            deleteStudentStatement = connection.prepareStatement(deleteStudentSql);

            connection.setAutoCommit(false); // Disable auto-commit

            for (int studentId : studentIds) {
                deleteStudentStatement.setInt(1, studentId);
                deleteStudentStatement.addBatch(); // Add query to the batch
            }

            int[] updateCounts = deleteStudentStatement.executeBatch(); // Execute batch queries

            connection.commit(); // Commit changes to the database

            System.out.println("Students successfully deleted.");
        } catch (SQLException e) {
            if (connection != null) {
                connection.rollback(); // Rollback changes in case of error
            }
            throw e;
        } finally {
            if (connection != null) {
                connection.close();
            }
            if (deleteStudentStatement != null) {
                deleteStudentStatement.close();
            }
        }
    }
}
