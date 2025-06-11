package com.jobportalapp.db;

import com.jobportalapp.db.DBConnection;
import com.jobportalapp.model.User;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class JobApplicationDAO {

    /**
     * Fetches all applicants who applied for a given job ID.
     *
     * @param jobId the ID of the job
     * @return List of User objects who applied for the job
     */
    public List<User> getApplicantsByJobId(int jobId) {
        List<User> applicants = new ArrayList<>();
        String sql = "SELECT u.id, u.name, u.email, u.status " +
                "FROM job_applications ja " +
                "JOIN users u ON ja.user_id = u.id " +
                "WHERE ja.job_id = ?";


        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, jobId);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    User user = new User();
                    user.setId(rs.getInt("id"));
                    user.setName(rs.getString("name"));
                    user.setEmail(rs.getString("email"));
                    user.setStatus(rs.getString("status")); // Ensure User class has this field
                    applicants.add(user);
                }
            }

        } catch (SQLException e) {
            System.err.println("Error fetching applicants: " + e.getMessage());
            e.printStackTrace();
        }

        return applicants;
    }

    /**
     * Allows a user to apply to a job if not already applied.
     *
     * @param userId the ID of the user
     * @param jobId the ID of the job
     * @return true if application is successful, false if already applied or error
     */
    public boolean applyToJob(int userId, int jobId) {
        String checkSql = "SELECT COUNT(*) FROM job_applications WHERE user_id = ? AND job_id = ?";
        String insertSql = "INSERT INTO job_applications (user_id, job_id, application_date) VALUES (?, ?, NOW())";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement checkStmt = conn.prepareStatement(checkSql)) {

            checkStmt.setInt(1, userId);
            checkStmt.setInt(2, jobId);

            try (ResultSet rs = checkStmt.executeQuery()) {
                if (rs.next() && rs.getInt(1) > 0) {
                    return false; // Already applied
                }
            }

            try (PreparedStatement insertStmt = conn.prepareStatement(insertSql)) {
                insertStmt.setInt(1, userId);
                insertStmt.setInt(2, jobId);
                int rowsAffected = insertStmt.executeUpdate();
                return rowsAffected > 0;
            }

        } catch (SQLException e) {
            System.err.println("Error applying to job: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }
}
