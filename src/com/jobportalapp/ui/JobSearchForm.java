package com.jobportalapp.ui;

import com.jobportalapp.db.DBConnection;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class JobSearchForm extends JFrame {

    private JTextField searchField;
    private JTable jobTable;
    private JScrollPane scrollPane;

    public JobSearchForm() {
        setTitle("Job Search - JobPortalApp");
        setSize(700, 450);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        // Background Panel
        BackgroundPanel bgPanel = new BackgroundPanel("images/background1.jpg");
        bgPanel.setLayout(new BorderLayout());

        // Transparent Panel for Components
        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setOpaque(false);
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Title Label
        JLabel titleLabel = new JLabel("Search for Jobs");
        titleLabel.setFont(new Font("SansSerif", Font.BOLD, 20));
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        mainPanel.add(titleLabel, BorderLayout.NORTH);

        // Search Input Panel
        JPanel inputPanel = new JPanel(new BorderLayout(5, 5));
        inputPanel.setOpaque(false);
        searchField = new JTextField();
        searchField.setFont(new Font("SansSerif", Font.PLAIN, 16));
        inputPanel.add(searchField, BorderLayout.CENTER);

        JButton searchButton = new JButton("Search");
        searchButton.setFont(new Font("SansSerif", Font.BOLD, 14));
        searchButton.addActionListener(e -> {
            String searchText = searchField.getText().trim();
            if (searchText.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Please enter a search term.", "Input Error", JOptionPane.WARNING_MESSAGE);
            } else {
                loadJobs(searchText);
            }
        });
        inputPanel.add(searchButton, BorderLayout.EAST);

        mainPanel.add(inputPanel, BorderLayout.CENTER);

        // Table Initialization
        jobTable = new JTable();
        jobTable.setFont(new Font("SansSerif", Font.PLAIN, 14));
        jobTable.setRowHeight(25);
        jobTable.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
        scrollPane = new JScrollPane(jobTable);

        mainPanel.add(scrollPane, BorderLayout.SOUTH);

        bgPanel.add(mainPanel, BorderLayout.CENTER);
        setContentPane(bgPanel);
        setVisible(true);
    }

    /**
     * Loads jobs from the database matching the search query.
     *
     * @param searchQuery The user's search input
     */
    private void loadJobs(String searchQuery) {
        List<String[]> jobs = getJobs(searchQuery);

        String[] columnNames = {"Job ID", "Title", "Company Name", "Location"};
        DefaultTableModel model = new DefaultTableModel(columnNames, 0);

        for (String[] job : jobs) {
            model.addRow(job);
        }

        jobTable.setModel(model);
    }

    /**
     * Retrieves matching job entries from the database.
     *
     * @param searchQuery The user's search input
     * @return A list of job rows as string arrays
     */
    private List<String[]> getJobs(String searchQuery) {
        List<String[]> jobs = new ArrayList<>();
        String query = """
            SELECT id, title, company_name, location
            FROM jobs
            WHERE title LIKE ? OR company_name LIKE ? OR location LIKE ?
        """;

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement ps = connection.prepareStatement(query)) {

            String likeQuery = "%" + searchQuery.trim() + "%";
            System.out.println("Executing query: " + query);  // Debugging log
            ps.setString(1, likeQuery);
            ps.setString(2, likeQuery);
            ps.setString(3, likeQuery);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    String[] job = {
                        String.valueOf(rs.getInt("id")),
                        rs.getString("title"),
                        rs.getString("company_name"),
                        rs.getString("location")
                    };
                    jobs.add(job);
                }
            }

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error fetching jobs: " + e.getMessage(),
                    "Database Error", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace(); // To help trace the issue in the logs
        }

        return jobs;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(JobSearchForm::new);
    }
}
