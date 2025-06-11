package com.jobportalapp.model;

public class Job {
    private int id;
    private int employerId;
    private String title;
    private String companyName;
    private String location;
    private double salary;
    private String description;
    private String status;

    // Constructors
    public Job() {}

    public Job(int id, int employerId, String title, String companyName, String location,
               double salary, String description, String status) {
        this.id = id;
        this.employerId = employerId;
        this.title = title;
        this.companyName = companyName;
        this.location = location;
        this.salary = salary;
        this.description = description;
        this.status = status;
    }

    // Getters and Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public int getEmployerId() { return employerId; }
    public void setEmployerId(int employerId) { this.employerId = employerId; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getCompanyName() { return companyName; }
    public void setCompanyName(String companyName) { this.companyName = companyName; }

    public String getLocation() { return location; }
    public void setLocation(String location) { this.location = location; }

    public double getSalary() { return salary; }
    public void setSalary(double salary) { this.salary = salary; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    @Override
    public String toString() {
        return "Job{" +
                "id=" + id +
                ", employerId=" + employerId +
                ", title='" + title + '\'' +
                ", companyName='" + companyName + '\'' +
                ", location='" + location + '\'' +
                ", salary=" + salary +
                ", description='" + description + '\'' +
                ", status='" + status + '\'' +
                '}';
    }
}
