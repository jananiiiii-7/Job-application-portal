package com.jobportalapp.ui;

import com.jobportalapp.db.DBConnection;
import com.jobportalapp.model.User;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class EmployerDashboard extends JFrame {

    private JTable jobTable;
    private JButton editButton, deleteButton, postJobButton;
    private int selectedJobId = -1;
    private final User currentUser;

    public EmployerDashboard(User user) {
        this.currentUser = user;
        setTitle("Employer Dashboard");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // Background panel setup
        BackgroundPanel bgPanel;
        try {
            bgPanel = new BackgroundPanel("images/background1.jpg");
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Failed to load background. Using default background.", "Warning", JOptionPane.WARNING_MESSAGE);
            bgPanel = new BackgroundPanel();
        }
        bgPanel.setLayout(new BorderLayout());

        JPanel panel = new JPanel(new BorderLayout());
        panel.setOpaque(false);

        // Title
        JLabel titleLabel = new JLabel("Your Posted Jobs");
        titleLabel.setFont(new Font("SansSerif", Font.BOLD, 20));
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        panel.add(titleLabel, BorderLayout.NORTH);

        // Job Table
        jobTable = new JTable();
        JScrollPane scrollPane = new JScrollPane(jobTable);
        panel.add(scrollPane, BorderLayout.CENTER);

        // Button Panel
        JPanel buttonPanel = new JPanel(new FlowLayout());
        postJobButton = new JButton("Post New Job");
        editButton = new JButton("Edit Job");
        deleteButton = new JButton("Delete Job");

        buttonPanel.add(postJobButton);
        buttonPanel.add(editButton);
        buttonPanel.add(deleteButton);
        panel.add(buttonPanel, BorderLayout.SOUTH);

        // Add listeners
        postJobButton.addActionListener(this::openPostJobForm);
        editButton.addActionListener(this::editSelectedJob);
        deleteButton.addActionListener(this::deleteSelectedJob);

        // Finalize UI
        bgPanel.add(panel, BorderLayout.CENTER);
        setContentPane(bgPanel);
        setVisible(true);

        refreshJobList();
    }

    private List<String[]> loadEmployerJobs() {
        List<String[]> jobListings = new ArrayList<>();
        String query = "SELECT id, title, location, salary, status FROM jobs WHERE employer_id = ?";

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement stmt = connection.prepareStatement(query)) {

            stmt.setInt(1, currentUser.getId());

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    String[] job = new String[5];
                    job[0] = String.valueOf(rs.getInt("id"));
                    job[1] = rs.getString("title");
                    job[2] = rs.getString("location");
                    job[3] = rs.getString("salary");
                    job[4] = rs.getString("status");
                    jobListings.add(job);
                }
            }

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error loading jobs from database.", "Database Error", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }

        return jobListings;
    }

    private void refreshJobList() {
        List<String[]> jobListings = loadEmployerJobs();
        String[] columnNames = {"Job ID", "Title", "Location", "Salary", "Status"};
        Object[][] data = new Object[jobListings.size()][columnNames.length];

        for (int i = 0; i < jobListings.size(); i++) {
            data[i] = jobListings.get(i);
        }

        DefaultTableModel model = new DefaultTableModel(data, columnNames) {
            public boolean isCellEditable(int row, int column) {
                return false; // prevent editing
            }
        };

        jobTable.setModel(model);
    }

    private void openPostJobForm(ActionEvent e) {
        try {
            new PostJobForm(currentUser);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error opening Post Job Form.", "Error", JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace();
        }
    }

    private void editSelectedJob(ActionEvent e) {
        int selectedRow = jobTable.getSelectedRow();
        if (selectedRow != -1) {
            selectedJobId = Integer.parseInt(jobTable.getValueAt(selectedRow, 0).toString());
            editJob(selectedJobId);
        } else {
            JOptionPane.showMessageDialog(this, "Please select a job to edit.", "Selection Error", JOptionPane.WARNING_MESSAGE);
        }
    }

    private void deleteSelectedJob(ActionEvent e) {
        int selectedRow = jobTable.getSelectedRow();
        if (selectedRow != -1) {
            selectedJobId = Integer.parseInt(jobTable.getValueAt(selectedRow, 0).toString());
            int confirm = JOptionPane.showConfirmDialog(this, "Are you sure you want to delete this job?", "Confirm Deletion", JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                deleteJob(selectedJobId);
            }
        } else {
            JOptionPane.showMessageDialog(this, "Please select a job to delete.", "Selection Error", JOptionPane.WARNING_MESSAGE);
        }
    }

    private void editJob(int jobId) {
        try {
            new PostJobForm(currentUser, jobId);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error opening Edit Job Form.", "Error", JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace();
        }
    }

    private void deleteJob(int jobId) {
        String query = "DELETE FROM jobs WHERE id = ? AND employer_id = ?";

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement stmt = connection.prepareStatement(query)) {

            stmt.setInt(1, jobId);
            stmt.setInt(2, currentUser.getId());

            int rowsDeleted = stmt.executeUpdate();

            if (rowsDeleted > 0) {
                JOptionPane.showMessageDialog(this, "Job deleted successfully.");
                refreshJobList();
            } else {
                JOptionPane.showMessageDialog(this, "Job not found or unauthorized deletion attempt.", "Error", JOptionPane.ERROR_MESSAGE);
            }

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error deleting job.", "Database Error", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }

    // Test main method
    public static void main(String[] args) {
        User testUser = new User(2, "Demo Employer", "employer@example.com", "employer", "active");
        SwingUtilities.invokeLater(() -> new EmployerDashboard(testUser));
    }
}
