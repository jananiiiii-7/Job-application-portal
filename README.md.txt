# 💼 JobPortalApp – Java Swing + MySQL Job Portal

A beginner-friendly, full-featured Job Portal application built using **Java Swing** for the frontend and **MySQL** for the backend. This project simulates a job marketplace where **job seekers** can explore and apply to jobs, **employers** can post and manage openings, and **admins** oversee the platform.

---

## 📌 Features

### 👤 Job Seeker
- Register and login
- View and search available jobs
- Apply for jobs
- View applied jobs

### 🏢 Employer
- Register and login
- Post new jobs
- Manage posted jobs
- View applicants for each job

### 🛠️ Admin
- View and manage all users
- Activate/deactivate accounts
- Remove or approve job posts
- View job applications

---

## 🧱 Tech Stack

| Layer         | Technology     |
|--------------|----------------|
| Frontend     | Java Swing (GUI) |
| Backend      | Java (JDBC, OOP) |
| Database     | MySQL           |
| IDE          | Eclipse         |
| Architecture | MVC + DAO Pattern |

---

## 🗃️ Database Schema

- `users` – stores user info with role (job_seeker/employer/admin)
- `jobs` – contains job postings with status (open/closed/removed)
- `job_applications` – stores application records (job_id, seeker_id)

---

## 📦 Folder Structure

com.jobportalapp

├── ui // All UI panels (LoginForm, Dashboards, Forms)

├── dao // DB interaction (UserDAO, JobDAO, ApplicationDAO)

├── model // Data models (User, Job, Application)

├── db // DB connection utility

├── util // Validators, Helpers, etc.

└── main.java // Entry point


---

## 🚀 Getting Started

### 1. Prerequisites
- Java JDK 8 or above
- Eclipse IDE
- MySQL Server
- MySQL JDBC Connector

### 2. Setup
Clone the repository:
   ```bash
   git clone https://github.com/your-username/JobPortalApp.git

Import into Eclipse as an existing Java project.

Set up the MySQL database using provided scripts:

CREATE DATABASE jobportal;
USE jobportal;

-- Tables: users, jobs, job_applications

Add the MySQL JDBC connector JAR to your project’s build path.

Run main.java to launch the application.
✨ Highlights
Clean OOP design with reusable components

Secure login flow for all roles

Modular DAO pattern for maintainable database interaction

Extensible: You can easily add pagination, job filters, or email integration

📸 Screenshots
You can add images from your project here to showcase GUI panels.

💡 Future Enhancements
Email verification & notifications

Resume uploads for job seekers

Admin analytics dashboard

Job search with advanced filters

🤝 Contributors
Dheeraj – Developer

Powered with ❤️ and Java.

📝 License
This project is open-source and available under the MIT License.