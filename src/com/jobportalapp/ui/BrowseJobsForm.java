package com.jobportalapp.ui;

import com.jobportalapp.db.DBConnection;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class BrowseJobsForm extends JFrame {

    private JTable jobTable;
    private JButton applyButton;
    private int userId; // Job Seeker's ID (passed after login)

    public BrowseJobsForm(int userId) {
        this.userId = userId;
        setTitle("Browse Jobs - JobPortalApp");
        setSize(700, 450);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // Background Panel
        BackgroundPanel bgPanel = new BackgroundPanel("images/background1.jpg");
        bgPanel.setLayout(new BorderLayout());

        // Main Panel
        JPanel panel = new JPanel(new BorderLayout());
        panel.setOpaque(false);

        // Title
        JLabel titleLabel = new JLabel("Available Jobs");
        titleLabel.setFont(new Font("SansSerif", Font.BOLD, 22));
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        panel.add(titleLabel, BorderLayout.NORTH);

        // Job Table
        List<String[]> jobs = loadJobs();
        String[] columnNames = {"Job ID", "Title", "Company", "Location"};
        Object[][] data = new Object[jobs.size()][4];

        for (int i = 0; i < jobs.size(); i++) {
            data[i] = jobs.get(i);
        }

        jobTable = new JTable(data, columnNames);
        JScrollPane scrollPane = new JScrollPane(jobTable);
        panel.add(scrollPane, BorderLayout.CENTER);

        // Apply Button
        applyButton = new JButton("Apply for Selected Job");
        applyButton.setFont(new Font("SansSerif", Font.BOLD, 16));
        applyButton.setForeground(Color.WHITE);
        applyButton.setBackground(new Color(0, 0, 0, 150));
        applyButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        panel.add(applyButton, BorderLayout.SOUTH);

        // Apply Action
        applyButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                applyToSelectedJob();
            }
        });

        bgPanel.add(panel, BorderLayout.CENTER);
        setContentPane(bgPanel);
        setVisible(true);
    }

    private List<String[]> loadJobs() {
        List<String[]> jobs = new ArrayList<>();
        String query = "SELECT id, title, company, location FROM jobs";

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement stmt = connection.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                String[] job = new String[4];
                job[0] = String.valueOf(rs.getInt("id"));
                job[1] = rs.getString("title");
                job[2] = rs.getString("company");
                job[3] = rs.getString("location");
                jobs.add(job);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return jobs;
    }

    private void applyToSelectedJob() {
        int selectedRow = jobTable.getSelectedRow();
        if (selectedRow != -1) {
            int jobId = Integer.parseInt(jobTable.getValueAt(selectedRow, 0).toString());

            // Insert into job_applications table
            String query = "INSERT INTO job_applications (user_id, job_id) VALUES (?, ?)";

            try (Connection connection = DBConnection.getConnection();
                 PreparedStatement stmt = connection.prepareStatement(query)) {

                stmt.setInt(1, userId);
                stmt.setInt(2, jobId);
                stmt.executeUpdate();

                JOptionPane.showMessageDialog(this, "Applied Successfully!", "Application Sent", JOptionPane.INFORMATION_MESSAGE);

            } catch (SQLException e) {
                JOptionPane.showMessageDialog(this, "You have already applied for this job.", "Application Error", JOptionPane.ERROR_MESSAGE);
                e.printStackTrace();
            }

        } else {
            JOptionPane.showMessageDialog(this, "Please select a job to apply.", "Selection Error", JOptionPane.WARNING_MESSAGE);
        }
    }

    public static void main(String[] args) {
        new BrowseJobsForm(2); // Example with Job Seeker ID 2
    }
}
