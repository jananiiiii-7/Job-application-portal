package com.jobportalapp.model;

public class JobApplication {
    private int id;
    private int jobId;
    private int userId;
    private String applicationDate;

    // Constructors
    public JobApplication() {}

    public JobApplication(int id, int jobId, int userId, String applicationDate) {
        this.id = id;
        this.jobId = jobId;
        this.userId = userId;
        this.applicationDate = applicationDate;
    }

    // Getters and Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public int getJobId() { return jobId; }
    public void setJobId(int jobId) { this.jobId = jobId; }

    public int getUserId() { return userId; }
    public void setUserId(int userId) { this.userId = userId; }

    public String getApplicationDate() { return applicationDate; }
    public void setApplicationDate(String applicationDate) { this.applicationDate = applicationDate; }
}
