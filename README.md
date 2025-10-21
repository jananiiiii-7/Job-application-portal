# Job-application-portal
Job Application Portal
Project Overview

The Job Application Portal is a desktop-based application developed using Java Swing, JDBC, and MySQL.
It is designed to streamline the job application process by connecting job seekers with employers on a single platform.
Users can post, browse, and apply for jobs easily while the system securely manages all application data.

Features

Separate Login & Registration: For job seekers and employers.

Job Management: Employers can post, update, and delete job listings.

Application Management: Job seekers can browse and apply for jobs.

Secure Database Handling: Uses PreparedStatement to prevent SQL injection.

Data Validation: Ensures proper login and registration credentials.

User-Friendly GUI: Clean interface designed with Java Swing.

Technology Stack

Frontend: Java Swing (Graphical User Interface)

Middleware: JDBC (Java Database Connectivity)

Backend: MySQL Database

System Flow

User Interaction: Users interact with a Java Swing GUI for login, registration, and job browsing.

Database Connectivity: Input data is sent to the MySQL database through JDBC.

Data Handling: Database operations like insert, update, delete, and fetch are performed.

Display Results: Processed information is displayed back to the user in the GUI.

Database Design

users: user_id, name, email, password, role

jobs: job_id, title, company, description, requirements

applications: app_id, job_id, user_id, status

Setup Instructions

Clone the repository

git clone https:///jananiiiii-7/Job-application-portal/.git


Import the project in your IDE (Eclipse, IntelliJ IDEA, or NetBeans).

Setup MySQL Database:

Create a database named job_portal.

Execute the provided SQL script database.sql to create tables.

Configure JDBC Connection:

Update the database URL, username, and password in the DBConnection.java file.

Run the Application:

Run Main.java to start the GUI.

Future Enhancements

Resume Keyword Scanner: To automatically match jobs with candidate skills.

Interview Scheduling System: Allow employers and applicants to schedule interviews.

Real-Time Application Tracker: Users can see status updates dynamically.

Analytics Dashboard: For employers to track application statistics and skill distribution.

Screenshots

(Include a few screenshots of your GUI – login, job listing, application submission.)

License

This project is licensed under the MIT License – see the LICENSE
 file for details.
