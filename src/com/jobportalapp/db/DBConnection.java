package com.jobportalapp.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnection {

    // Database credentials
    private static final String URL = "jdbc:mysql://localhost:3306/job_portal_db";
    private static final String USER = "root"; // Replace with your MySQL username
    private static final String PASSWORD = "Admin123"; // Replace with your MySQL password

    // Method to get the database connection
    public static Connection getConnection() throws SQLException {
        try {
            // Register MySQL JDBC driver (this is optional for newer versions of JDBC)
            Class.forName("com.mysql.cj.jdbc.Driver");
            // Return the connection to the database
            return DriverManager.getConnection(URL, USER, PASSWORD);
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
            throw new SQLException("Unable to connect to the database", e);
        }
    }
}
