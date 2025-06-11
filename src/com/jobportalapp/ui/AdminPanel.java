package com.jobportalapp.ui;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;
import com.jobportalapp.db.DBConnection;
import com.jobportalapp.model.User;
import com.jobportalapp.utils.Session;

public class AdminPanel extends JFrame {
    private JTabbedPane tabbedPane;
    private JTable userTable, jobTable, applicationTable;
    private DefaultTableModel userModel, jobModel, applicationModel;
    private JButton deleteUserBtn, activateUserBtn, removeJobBtn, approveJobBtn, rejectJobBtn;

    public AdminPanel() {
        setTitle("Admin Panel - JobPortalApp");
        setSize(800, 600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        User currentUser = Session.getCurrentUser();
        tabbedPane = new JTabbedPane();

        initUsersTab();
        initJobsTab();
        initApplicationsTab();

        add(tabbedPane);
        setVisible(true);
    }

    // ------------------ USERS TAB ------------------
    private void initUsersTab() {
        JPanel panel = new JPanel(new BorderLayout());

        userModel = new DefaultTableModel(new String[]{"ID", "Name", "Email", "Role", "Status"}, 0);
        userTable = new JTable(userModel);
        JScrollPane scrollPane = new JScrollPane(userTable);

        JPanel buttonPanel = new JPanel();
        deleteUserBtn = new JButton("Deactivate User");
        activateUserBtn = new JButton("Activate User");

        deleteUserBtn.addActionListener(e -> updateUserStatus("inactive"));
        activateUserBtn.addActionListener(e -> updateUserStatus("active"));

        buttonPanel.add(deleteUserBtn);
        buttonPanel.add(activateUserBtn);

        panel.add(scrollPane, BorderLayout.CENTER);
        panel.add(buttonPanel, BorderLayout.SOUTH);

        tabbedPane.addTab("Users", panel);
        loadUsers();
    }

    // ------------------ JOBS TAB ------------------
    private void initJobsTab() {
        JPanel panel = new JPanel(new BorderLayout());

        jobModel = new DefaultTableModel(new String[]{"ID", "Title", "Company", "Location", "Status"}, 0);
        jobTable = new JTable(jobModel);
        JScrollPane scrollPane = new JScrollPane(jobTable);

        JPanel buttonPanel = new JPanel();
        removeJobBtn = new JButton("Remove Job");
        approveJobBtn = new JButton("Approve Job");
        rejectJobBtn = new JButton("Reject Job");

        removeJobBtn.addActionListener(e -> updateJobStatus("removed"));
        approveJobBtn.addActionListener(e -> updateJobStatus("active"));
        rejectJobBtn.addActionListener(e -> updateJobStatus("rejected"));

        buttonPanel.add(approveJobBtn);
        buttonPanel.add(rejectJobBtn);
        buttonPanel.add(removeJobBtn);

        panel.add(scrollPane, BorderLayout.CENTER);
        panel.add(buttonPanel, BorderLayout.SOUTH);

        tabbedPane.addTab("Jobs", panel);
        loadJobs();
    }

    // ------------------ APPLICATIONS TAB ------------------
    private void initApplicationsTab() {
        JPanel panel = new JPanel(new BorderLayout());

        applicationModel = new DefaultTableModel(
            new String[]{"Application ID", "Job Title", "Applicant"}, 0);
        applicationTable = new JTable(applicationModel);
        JScrollPane scrollPane = new JScrollPane(applicationTable);

        panel.add(scrollPane, BorderLayout.CENTER);
        tabbedPane.addTab("Applications", panel);

        loadApplications();
    }

    private void loadApplications() {
        try (Connection con = DBConnection.getConnection();
             Statement stmt = con.createStatement();
             ResultSet rs = stmt.executeQuery(
                 "SELECT a.id, j.title, u.name " +
                 "FROM job_applications a " +
                 "JOIN users u ON a.user_id = u.id " +
                 "JOIN jobs j ON a.job_id = j.id")) {

            applicationModel.setRowCount(0);
            while (rs.next()) {
                applicationModel.addRow(new Object[]{
                    rs.getInt("id"),
                    rs.getString("title"),
                    rs.getString("name")
                });
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Failed to load applications: " + e.getMessage());
        }
    }

    // ------------------ LOAD USERS ------------------
    private void loadUsers() {
        try (Connection con = DBConnection.getConnection();
             Statement stmt = con.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT id, name, email, role, status FROM users WHERE role != 'admin'")) {

            userModel.setRowCount(0);
            while (rs.next()) {
                userModel.addRow(new Object[]{
                    rs.getInt("id"),
                    rs.getString("name"),
                    rs.getString("email"),
                    rs.getString("role"),
                    rs.getString("status")
                });
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Failed to load users: " + e.getMessage());
        }
    }

    // ------------------ LOAD JOBS ------------------
    private void loadJobs() {
        try (Connection con = DBConnection.getConnection();
             Statement stmt = con.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT id, title, company_name, location, status FROM jobs")) {

            jobModel.setRowCount(0);
            while (rs.next()) {
                jobModel.addRow(new Object[]{
                    rs.getInt("id"),
                    rs.getString("title"),
                    rs.getString("company_name"),
                    rs.getString("location"),
                    rs.getString("status")
                });
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Failed to load jobs: " + e.getMessage());
        }
    }

    // ------------------ UPDATE USER STATUS ------------------
    private void updateUserStatus(String newStatus) {
        int row = userTable.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(this, "Please select a user to update.");
            return;
        }

        int userId = (int) userModel.getValueAt(row, 0);
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement("UPDATE users SET status = ? WHERE id = ?")) {

            ps.setString(1, newStatus);
            ps.setInt(2, userId);
            ps.executeUpdate();

            JOptionPane.showMessageDialog(this, "User status updated to " + newStatus + ".");
            loadUsers();

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Failed to update user: " + e.getMessage());
        }
    }

    // ------------------ UPDATE JOB STATUS ------------------
    private void updateJobStatus(String newStatus) {
        int row = jobTable.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(this, "Please select a job to update.");
            return;
        }

        int jobId = (int) jobModel.getValueAt(row, 0);
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement("UPDATE jobs SET status = ? WHERE id = ?")) {

            ps.setString(1, newStatus);
            ps.setInt(2, jobId);
            ps.executeUpdate();

            JOptionPane.showMessageDialog(this, "Job status updated to " + newStatus + ".");
            loadJobs();

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Failed to update job: " + e.getMessage());
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(AdminPanel::new);
    }
}
