package com.jobportalapp.db;

import com.jobportalapp.model.Job;
import com.jobportalapp.model.User;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class AdminDAO {

    // ✅ Get all users
    public static List<User> getAllUsers() {
        List<User> userList = new ArrayList<>();
        String sql = "SELECT * FROM users";

        try (Connection conn = DBConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                User user = new User(
                    rs.getInt("id"),
                    rs.getString("name"),
                    rs.getString("email"),
                    rs.getString("password"),
                    rs.getString("role")
                );
                userList.add(user);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return userList;
    }

    // ✅ Get all jobs
    public static List<Job> getAllJobs() {
        List<Job> jobList = new ArrayList<>();
        String sql = "SELECT * FROM jobs";

        try (Connection conn = DBConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                Job job = new Job(
                    rs.getInt("id"),
                    rs.getInt("employer_id"),
                    rs.getString("title"),
                    rs.getString("company_name"),
                    rs.getString("location"),
                    rs.getDouble("salary"),
                    rs.getString("description"),
                    rs.getString("status")
                );
                jobList.add(job);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return jobList;
    }

    // ✅ Delete a user by ID
    public static boolean deleteUser(int userId) {
        String sql = "DELETE FROM users WHERE id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, userId);
            return stmt.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // ✅ Delete a job by ID
    public static boolean deleteJob(int jobId) {
        String sql = "DELETE FROM jobs WHERE id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, jobId);
            return stmt.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}
