package com.jobportalapp.ui;

import com.jobportalapp.model.User;
import com.jobportalapp.db.JobApplicationDAO;


import javax.swing.*;
import java.awt.*;
import java.util.List;

public class ViewApplicantsForm extends JFrame {

    private JTable applicantsTable;

    public ViewApplicantsForm(int jobId) {
        setTitle("Applicants for Job ID: " + jobId);
        setSize(600, 400); // Slightly wider to accommodate 'Status'
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        // Background panel
        BackgroundPanel bgPanel = new BackgroundPanel("images/background1.jpg");
        bgPanel.setLayout(new BorderLayout());

        JPanel panel = new JPanel(new BorderLayout());
        panel.setOpaque(false);

        // Title Label
        JLabel titleLabel = new JLabel("Applicants");
        titleLabel.setFont(new Font("SansSerif", Font.BOLD, 18));
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        panel.add(titleLabel, BorderLayout.NORTH);

        // Load applicants
        List<User> applicants = loadApplicants(jobId);

        String[] columnNames = {"Applicant ID", "Name", "Email", "Status"};
        Object[][] data = new Object[applicants.size()][4];

        for (int i = 0; i < applicants.size(); i++) {
            User user = applicants.get(i);
            data[i][0] = user.getId();
            data[i][1] = user.getName();
            data[i][2] = user.getEmail();
            data[i][3] = user.getStatus(); // ✅ Include status
        }

        applicantsTable = new JTable(data, columnNames);
        JScrollPane scrollPane = new JScrollPane(applicantsTable);
        panel.add(scrollPane, BorderLayout.CENTER);

        bgPanel.add(panel, BorderLayout.CENTER);
        setContentPane(bgPanel);
        setVisible(true);
    }

    private List<User> loadApplicants(int jobId) {
        JobApplicationDAO dao = new JobApplicationDAO();
        return dao.getApplicantsByJobId(jobId);
    }

    public static void main(String[] args) {
        new ViewApplicantsForm(1); // Test with dummy job ID
    }
}
