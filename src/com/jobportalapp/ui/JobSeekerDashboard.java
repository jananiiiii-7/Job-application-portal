package com.jobportalapp.ui;

import com.jobportalapp.db.DBConnection;
import com.jobportalapp.model.User;
import com.jobportalapp.utils.Session;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class JobSeekerDashboard extends JFrame {

    private JTable jobTable;
    private JButton applyButton;

    public JobSeekerDashboard(User currentUser) {
        setTitle("Job Seeker Dashboard - JobPortalApp");
        setSize(800, 550);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        if (currentUser == null) {
            JOptionPane.showMessageDialog(this, "User not logged in. Redirecting to login page...");
            dispose();
            return;
        }

        BackgroundPanel bgPanel = new BackgroundPanel("images/background1.jpg");
        bgPanel.setLayout(new BorderLayout());

        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setOpaque(false);

        JLabel titleLabel = new JLabel("Explore Opportunities");
        titleLabel.setFont(new Font("SansSerif", Font.BOLD, 24));
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        mainPanel.add(titleLabel, BorderLayout.NORTH);

        String[] columnNames = {"Job ID", "Title", "Company", "Location", "Salary", "Applicants"};
        DefaultTableModel model = new DefaultTableModel(columnNames, 0) {
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        List<String[]> jobListings = loadJobListings();
        for (String[] job : jobListings) {
            model.addRow(job);
        }

        jobTable = new JTable(model);
        jobTable.setFont(new Font("SansSerif", Font.PLAIN, 14));
        jobTable.setRowHeight(25);
        JScrollPane scrollPane = new JScrollPane(jobTable);
        mainPanel.add(scrollPane, BorderLayout.CENTER);

        applyButton = createStyledButton("Apply for Selected Job");
        applyButton.setToolTipText("Submit your application for the selected job.");
        applyButton.addActionListener(e -> handleJobApplication(currentUser));
        mainPanel.add(applyButton, BorderLayout.SOUTH);

        bgPanel.add(mainPanel, BorderLayout.CENTER);
        setContentPane(bgPanel);
        setVisible(true);
    }

    private List<String[]> loadJobListings() {
        List<String[]> jobListings = new ArrayList<>();
        String query = "SELECT j.id, j.title, j.company_name, j.location, j.salary, COUNT(ja.job_seeker_id) as applicants " +
                       "FROM jobs j " +
                       "LEFT JOIN job_applications ja ON j.id = ja.job_id " +
                       "WHERE j.status = 'open' " +
                       "GROUP BY j.id, j.title, j.company_name, j.location, j.salary";


        try (Connection connection = DBConnection.getConnection();
             Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {

            while (rs.next()) {
                String[] job = {
                        String.valueOf(rs.getInt("id")),
                        rs.getString("title"),
                        rs.getString("company_name"),
                        rs.getString("location"),
                        String.format("%.2f", rs.getDouble("salary")),
                        String.valueOf(rs.getInt("applicants"))
                };
                jobListings.add(job);
            }

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Failed to load job listings: " + e.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }

        return jobListings;
    }

    private void handleJobApplication(User currentUser) {
        int selectedRow = jobTable.getSelectedRow();
        if (selectedRow != -1) {
            int jobId = Integer.parseInt(jobTable.getValueAt(selectedRow, 0).toString());
            if (currentUser == null) {
                JOptionPane.showMessageDialog(this, "Session expired. Please login again.", "Session Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            // Optional enhancement: check if job is still active
            if (!isJobActive(jobId)) {
                JOptionPane.showMessageDialog(this, "This job is no longer active.", "Job Closed", JOptionPane.WARNING_MESSAGE);
                return;
            }

            applyForJob(currentUser.getId(), jobId);
        } else {
            JOptionPane.showMessageDialog(this, "Please select a job to apply for.", "No Selection", JOptionPane.WARNING_MESSAGE);
        }
    }

    private boolean isJobActive(int jobId) {
        String query = "SELECT status FROM jobs WHERE id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setInt(1, jobId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return "open".equalsIgnoreCase(rs.getString("status"));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    private void applyForJob(int userId, int jobId) {
        String checkQuery = "SELECT COUNT(*) FROM job_applications WHERE job_seeker_id = ? AND job_id = ?";
        String insertQuery = "INSERT INTO job_applications (job_seeker_id, job_id, application_date) VALUES (?, ?, NOW())";

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement checkStmt = connection.prepareStatement(checkQuery)) {

            checkStmt.setInt(1, userId);
            checkStmt.setInt(2, jobId);
            ResultSet rs = checkStmt.executeQuery();

            if (rs.next() && rs.getInt(1) > 0) {
                JOptionPane.showMessageDialog(this, "You have already applied for this job.", "Duplicate Application", JOptionPane.WARNING_MESSAGE);
                return;
            }

            try (PreparedStatement insertStmt = connection.prepareStatement(insertQuery)) {
                insertStmt.setInt(1, userId);
                insertStmt.setInt(2, jobId);
                insertStmt.executeUpdate();

                JOptionPane.showMessageDialog(this, "Application submitted successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
            }

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error while applying for the job.", "Database Error", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }

    private JButton createStyledButton(String text) {
        JButton button = new JButton(text);
        button.setFocusPainted(false);
        button.setBackground(new Color(0, 0, 0, 170));
        button.setForeground(Color.WHITE);
        button.setFont(new Font("SansSerif", Font.BOLD, 16));
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        return button;
    }

    public static void main(String[] args) {
        // For testing: Simulating a user session
        User dummyUser = new User();
        dummyUser.setId(1);
        dummyUser.setName("Test User");
        dummyUser.setRole("job_seeker");
        Session.setCurrentUser(dummyUser);

        SwingUtilities.invokeLater(() -> new JobSeekerDashboard(Session.getCurrentUser()));
    }
}