package com.jobportalapp.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnection {

    // Database credentials
    private static final String URL = "jdbc:mysql://localhost:3306/job_portal_db";
    private static final String USER = "root"; // Replace with your MySQL username
    private static final String PASSWORD = "root"; // Replace with your MySQL password

    // Method to get the database connection
    public static Connection getConnection() throws SQLException {
        try {
            // Return the connection to the database
            return DriverManager.getConnection(URL, USER, PASSWORD);
        } catch (SQLException e) {
            e.printStackTrace();
            throw new SQLException("Unable to connect to the database", e);
        }
    }
}
