package com.jobportalapp.db;



import com.jobportalapp.db.DBConnection;
import com.jobportalapp.model.Job;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class JobDAO {

    // Insert a new job into the database with employer ID and default 'open' status
    public boolean addJob(String title, String companyName, String location, double salary, String description, int employerId) {
        String query = "INSERT INTO jobs (title, company_name, location, salary, description, employer_id, status) VALUES (?, ?, ?, ?, ?, ?, 'open')";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, title);
            stmt.setString(2, companyName);
            stmt.setString(3, location);
            stmt.setDouble(4, salary);
            stmt.setString(5, description);
            stmt.setInt(6, employerId);

            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }

    // Fetch only jobs with status 'open' (for job seekers)
    public List<Job> getAllOpenJobs() {
        List<Job> jobs = new ArrayList<>();
        String query = "SELECT * FROM jobs WHERE status = 'open'";

        try (Connection conn = DBConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {

            while (rs.next()) {
                Job job = new Job();
                job.setId(rs.getInt("id"));
                job.setEmployerId(rs.getInt("employer_id"));
                job.setTitle(rs.getString("title"));
                job.setCompanyName(rs.getString("company_name"));
                job.setLocation(rs.getString("location"));
                job.setSalary(rs.getDouble("salary"));
                job.setDescription(rs.getString("description"));
                job.setStatus(rs.getString("status"));
                jobs.add(job);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return jobs;
    }

    // You can add more methods here: updateJobStatus, deleteJobById, getJobsByEmployer, etc.
}
