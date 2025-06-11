package com.jobportalapp.ui;

import com.jobportalapp.db.DBConnection;

import javax.swing.*;
import java.awt.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class JobApplicationForm extends JFrame {

    private final JButton applyButton;
    private final int jobId;
    private final int userId;  // Assumed to be passed in production via session or login module

    public JobApplicationForm(int jobId, int userId) {
        this.jobId = jobId;
        this.userId = userId;

        setTitle("Apply for Job ID: " + jobId);
        setSize(450, 300);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        // Background Panel
        BackgroundPanel bgPanel = new BackgroundPanel("images/background1.jpg");
        bgPanel.setLayout(new BorderLayout());

        // Transparent Panel
        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setOpaque(false);
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Title Label
        JLabel titleLabel = new JLabel("Apply for Job ID: " + jobId);
        titleLabel.setFont(new Font("SansSerif", Font.BOLD, 20));
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        mainPanel.add(titleLabel, BorderLayout.NORTH);

        // Apply Button
        applyButton = new JButton("Apply Now");
        applyButton.setFont(new Font("SansSerif", Font.BOLD, 16));
        applyButton.setBackground(new Color(0, 0, 0, 150));
        applyButton.setForeground(Color.WHITE);
        applyButton.setFocusPainted(false);
        applyButton.addActionListener(e -> applyForJob());

        mainPanel.add(applyButton, BorderLayout.CENTER);
        bgPanel.add(mainPanel, BorderLayout.CENTER);

        setContentPane(bgPanel);
        setVisible(true);
    }

    /**
     * Inserts a job application for the current user and job.
     */
    private void applyForJob() {
        String query = "INSERT INTO job_applications (user_id, job_id) VALUES (?, ?)";

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement ps = connection.prepareStatement(query)) {

            ps.setInt(1, userId);
            ps.setInt(2, jobId);

            int rowsAffected = ps.executeUpdate();
            if (rowsAffected > 0) {
                JOptionPane.showMessageDialog(this,
                        "✅ Application submitted successfully!",
                        "Success", JOptionPane.INFORMATION_MESSAGE);
                dispose(); // Close window after applying
            } else {
                JOptionPane.showMessageDialog(this,
                        "⚠️ Unable to apply for the job. Please try again.",
                        "Failure", JOptionPane.WARNING_MESSAGE);
            }

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this,
                    "❌ Error while applying: " + e.getMessage(),
                    "Database Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new JobApplicationForm(1, 1)); // Example call
    }
}
