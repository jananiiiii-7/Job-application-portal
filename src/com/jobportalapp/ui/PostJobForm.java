package com.jobportalapp.ui;

import com.jobportalapp.db.DBConnection;
import com.jobportalapp.model.User;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class PostJobForm extends JFrame {

    private JTextField titleField, companyNameField, locationField, salaryField;
    private JTextArea descriptionArea;
    private JButton postButton;
    private int employerId;
    private User currentUser;

    // Constructor used in main
    public PostJobForm(int employerId) {
        this.employerId = employerId;
        initializeUI();
    }

    // Constructor for future use if only User object is passed
    public PostJobForm(User user) {
        this.currentUser = user;
        this.employerId = user.getId(); // Assumes User has getId()
        initializeUI();
    }

    // Constructor with both User and explicit employerId
    public PostJobForm(User user, int employerId) {
        this.currentUser = user;
        this.employerId = employerId;
        initializeUI();
    }

    private void initializeUI() {
        setTitle("Post a New Job");
        setSize(500, 600);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        BackgroundPanel bgPanel = new BackgroundPanel("images/background1.jpg");
        bgPanel.setLayout(new BorderLayout());

        JPanel formPanel = new JPanel(new GridBagLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                setOpaque(false);
            }
        };
        formPanel.setOpaque(false);
        formPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(10, 10, 10, 10);

        Font labelFont = new Font("SansSerif", Font.BOLD, 16);
        Font fieldFont = new Font("SansSerif", Font.PLAIN, 14);

        // Job Title
        gbc.gridx = 0; gbc.gridy = 0;
        JLabel titleLabel = new JLabel("Job Title:");
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setFont(labelFont);
        formPanel.add(titleLabel, gbc);

        gbc.gridx = 1;
        titleField = createStyledTextField();
        formPanel.add(titleField, gbc);

        // Company Name
        gbc.gridx = 0; gbc.gridy = 1;
        JLabel companyNameLabel = new JLabel("Company Name:");
        companyNameLabel.setForeground(Color.WHITE);
        companyNameLabel.setFont(labelFont);
        formPanel.add(companyNameLabel, gbc);

        gbc.gridx = 1;
        companyNameField = createStyledTextField();
        formPanel.add(companyNameField, gbc);

        // Location
        gbc.gridx = 0; gbc.gridy = 2;
        JLabel locationLabel = new JLabel("Location:");
        locationLabel.setForeground(Color.WHITE);
        locationLabel.setFont(labelFont);
        formPanel.add(locationLabel, gbc);

        gbc.gridx = 1;
        locationField = createStyledTextField();
        formPanel.add(locationField, gbc);

        // Salary
        gbc.gridx = 0; gbc.gridy = 3;
        JLabel salaryLabel = new JLabel("Salary:");
        salaryLabel.setForeground(Color.WHITE);
        salaryLabel.setFont(labelFont);
        formPanel.add(salaryLabel, gbc);

        gbc.gridx = 1;
        salaryField = createStyledTextField();
        formPanel.add(salaryField, gbc);

        // Description
        gbc.gridx = 0; gbc.gridy = 4;
        gbc.anchor = GridBagConstraints.NORTH;
        JLabel descriptionLabel = new JLabel("Description:");
        descriptionLabel.setForeground(Color.WHITE);
        descriptionLabel.setFont(labelFont);
        formPanel.add(descriptionLabel, gbc);

        gbc.gridx = 1;
        descriptionArea = new JTextArea(5, 15);
        descriptionArea.setLineWrap(true);
        descriptionArea.setWrapStyleWord(true);
        descriptionArea.setOpaque(false);
        descriptionArea.setForeground(Color.WHITE);
        descriptionArea.setCaretColor(Color.WHITE);
        descriptionArea.setFont(fieldFont);
        JScrollPane descriptionScroll = new JScrollPane(descriptionArea);
        descriptionScroll.setOpaque(false);
        descriptionScroll.getViewport().setOpaque(false);
        formPanel.add(descriptionScroll, gbc);

        // Post Button
        gbc.gridx = 0; gbc.gridy = 5;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        postButton = createStyledButton("Post Job");
        formPanel.add(postButton, gbc);

        // Action Listener
        postButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                postJob();
            }
        });

        bgPanel.add(formPanel, BorderLayout.CENTER);
        setContentPane(bgPanel);
        setVisible(true);
    }

    private JTextField createStyledTextField() {
        JTextField field = new JTextField(15);
        field.setOpaque(false);
        field.setForeground(Color.WHITE);
        field.setBorder(BorderFactory.createMatteBorder(0, 0, 2, 0, Color.WHITE));
        field.setCaretColor(Color.WHITE);
        field.setFont(new Font("SansSerif", Font.PLAIN, 14));
        return field;
    }

    private JButton createStyledButton(String text) {
        JButton button = new JButton(text);
        button.setFocusPainted(false);
        button.setBackground(new Color(0, 0, 0, 150));
        button.setForeground(Color.WHITE);
        button.setFont(new Font("SansSerif", Font.BOLD, 16));
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        return button;
    }

    private void postJob() {
        String title = titleField.getText().trim();
        String companyName = companyNameField.getText().trim();
        String location = locationField.getText().trim();
        String salary = salaryField.getText().trim();
        String description = descriptionArea.getText().trim();

        if (title.isEmpty() || companyName.isEmpty() || location.isEmpty() || salary.isEmpty() || description.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please fill all fields.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        try {
            Double.parseDouble(salary);
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Please enter a valid numeric salary.", "Validation Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        String sql = "INSERT INTO jobs (title, company_name, location, salary, description, employer_id, status) VALUES (?, ?, ?, ?, ?, ?, ?)";

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement stmt = connection.prepareStatement(sql)) {

            stmt.setString(1, title);
            stmt.setString(2, companyName);
            stmt.setString(3, location);
            stmt.setString(4, salary);
            stmt.setString(5, description);
            stmt.setInt(6, employerId);
            stmt.setString(7, "open");  // Default status as 'open'

            int rowsInserted = stmt.executeUpdate();
            if (rowsInserted > 0) {
                int choice = JOptionPane.showConfirmDialog(this,
                        "Job posted successfully!\n\nThe job is now open and visible to job seekers.\n\nPost another?",
                        "Success", JOptionPane.YES_NO_OPTION);

                if (choice == JOptionPane.YES_OPTION) {
                    titleField.setText("");
                    companyNameField.setText("");
                    locationField.setText("");
                    salaryField.setText("");
                    descriptionArea.setText("");
                } else {
                    dispose();
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error posting job: " + e.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    public static void main(String[] args) {
        new PostJobForm(1); // For testing
    }
}
