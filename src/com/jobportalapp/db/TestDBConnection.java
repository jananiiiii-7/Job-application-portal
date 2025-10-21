package com.jobportalapp.db;


import java.sql.Connection;
import java.sql.SQLException;

public class TestDBConnection {
    public static void main(String[] args) {
        try (Connection con = DBConnection.getConnection()) {
            if (con != null) {
                System.out.println("✅ Database connection successful!");
            } else {
                System.out.println("❌ Connection returned null.");
            }
        } catch (SQLException e) {
            System.out.println("❌ Database connection failed!");
            e.printStackTrace();
        }
    }
}

