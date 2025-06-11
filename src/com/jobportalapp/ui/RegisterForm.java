package com.jobportalapp.ui;

import com.jobportalapp.db.DBConnection;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import javax.swing.*;
import java.awt.*;

public class RegisterForm extends JFrame {

    private JTextField nameField, emailField;
    private JPasswordField passwordField;
    private JComboBox<String> roleCombo;
    private JButton registerButton, loginRedirectButton;

    public RegisterForm() {
        setTitle("Register - JobPortalApp");
        setSize(450, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // Create BackgroundPanel and set it as the content pane
        BackgroundPanel bgPanel = new BackgroundPanel("images/background1.jpg"); // Replace with correct image path
        bgPanel.setLayout(new BorderLayout());

        // Transparent form panel
        JPanel formPanel = new JPanel(new GridBagLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                setOpaque(false);
            }
        };
        formPanel.setOpaque(false);
        formPanel.setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30));

        // Font Settings
        Font titleFont = new Font("SansSerif", Font.BOLD, 26);
        Font labelFont = new Font("SansSerif", Font.BOLD, 16);
        Font fieldFont = new Font("SansSerif", Font.PLAIN, 14);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(10, 10, 10, 10);

        // Title
        JLabel titleLabel = new JLabel("Create an Account");
        titleLabel.setFont(titleFont);
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);

        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        formPanel.add(titleLabel, gbc);

        gbc.gridwidth = 1;

        // Name
        gbc.gridx = 0;
        gbc.gridy = 1;
        JLabel nameLabel = new JLabel("Name:");
        nameLabel.setForeground(Color.WHITE);
        nameLabel.setFont(labelFont);
        formPanel.add(nameLabel, gbc);

        gbc.gridx = 1;
        nameField = createStyledTextField();
        formPanel.add(nameField, gbc);

        // Email
        gbc.gridx = 0;
        gbc.gridy = 2;
        JLabel emailLabel = new JLabel("Email:");
        emailLabel.setForeground(Color.WHITE);
        emailLabel.setFont(labelFont);
        formPanel.add(emailLabel, gbc);

        gbc.gridx = 1;
        emailField = createStyledTextField();
        formPanel.add(emailField, gbc);

        // Password
        gbc.gridx = 0;
        gbc.gridy = 3;
        JLabel passwordLabel = new JLabel("Password:");
        passwordLabel.setForeground(Color.WHITE);
        passwordLabel.setFont(labelFont);
        formPanel.add(passwordLabel, gbc);

        gbc.gridx = 1;
        passwordField = new JPasswordField(15);
        stylePasswordField(passwordField);
        formPanel.add(passwordField, gbc);

        // Role
        gbc.gridx = 0;
        gbc.gridy = 4;
        JLabel roleLabel = new JLabel("Role:");
        roleLabel.setForeground(Color.WHITE);
        roleLabel.setFont(labelFont);
        formPanel.add(roleLabel, gbc);

        gbc.gridx = 1;
        roleCombo = new JComboBox<>(new String[]{"job_seeker", "employer"});
        roleCombo.setFont(fieldFont);
        roleCombo.setOpaque(false);
        formPanel.add(roleCombo, gbc);

        // Register Button
        gbc.gridx = 0;
        gbc.gridy = 5;
        gbc.gridwidth = 2;
        registerButton = createStyledButton("Register");
        formPanel.add(registerButton, gbc);

        // Login Redirect Button
        gbc.gridy = 6;
        loginRedirectButton = createTransparentButton("Already have an account? Login");
        formPanel.add(loginRedirectButton, gbc);

        bgPanel.add(formPanel, BorderLayout.CENTER);

        setContentPane(bgPanel);
        setVisible(true);

        // Add ActionListener to Register Button
        registerButton.addActionListener(e -> registerUser());

        // Add ActionListener to Login Redirect Button
        loginRedirectButton.addActionListener(e -> redirectToLogin());
    }

    private void registerUser() {
        // Get user input
        String name = nameField.getText();
        String email = emailField.getText();
        String password = new String(passwordField.getPassword());
        String role = (String) roleCombo.getSelectedItem();

        // Check if any field is empty
        if (name.isEmpty() || email.isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(this, "All fields must be filled!", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Check if email format is valid
        if (!email.matches("^[A-Za-z0-9+_.-]+@(.+)$")) {
            JOptionPane.showMessageDialog(this, "Invalid email format!", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Connect to the database and insert data
        try (Connection conn = DBConnection.getConnection()) {
            String sql = "INSERT INTO users (name, email, password, role) VALUES (?, ?, ?, ?)";

            try (PreparedStatement pst = conn.prepareStatement(sql)) {
                pst.setString(1, name);
                pst.setString(2, email);
                pst.setString(3, password);  // Store the password without hashing
                pst.setString(4, role);

                int rowsAffected = pst.executeUpdate();
                if (rowsAffected > 0) {
                    JOptionPane.showMessageDialog(this, "Registration Successful!", "Success", JOptionPane.INFORMATION_MESSAGE);
                    // Optionally, redirect to login screen
                    dispose(); // Close current RegisterForm window
                    new LoginForm(); // Open login form
                } else {
                    JOptionPane.showMessageDialog(this, "Registration Failed. Try again.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }

        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Database error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private JTextField createStyledTextField() {
        JTextField textField = new JTextField(15);
        textField.setOpaque(false);
        textField.setForeground(Color.WHITE);
        textField.setBorder(BorderFactory.createMatteBorder(0, 0, 2, 0, Color.WHITE));
        textField.setCaretColor(Color.WHITE);
        textField.setFont(new Font("SansSerif", Font.PLAIN, 14));
        return textField;
    }

    private void stylePasswordField(JPasswordField passwordField) {
        passwordField.setOpaque(false);
        passwordField.setForeground(Color.WHITE);
        passwordField.setBorder(BorderFactory.createMatteBorder(0, 0, 2, 0, Color.WHITE));
        passwordField.setCaretColor(Color.WHITE);
        passwordField.setFont(new Font("SansSerif", Font.PLAIN, 14));
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

    private JButton createTransparentButton(String text) {
        JButton button = new JButton(text);
        button.setBorderPainted(false);
        button.setContentAreaFilled(false);
        button.setFocusPainted(false);
        button.setForeground(Color.WHITE);
        button.setFont(new Font("SansSerif", Font.PLAIN, 13));
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        return button;
    }

    private void redirectToLogin() {
        dispose(); // Close RegisterForm
        new LoginForm(); // Open LoginForm
    }

    public static void main(String[] args) {
        new RegisterForm();
    }
}
