-- Job Portal Database Schema
-- Create database
CREATE DATABASE IF NOT EXISTS job_portal_db;
USE job_portal_db;

-- Users table
CREATE TABLE IF NOT EXISTS users (
    id INT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(50) UNIQUE NOT NULL,
    password VARCHAR(255) NOT NULL,
    email VARCHAR(100) UNIQUE NOT NULL,
    role ENUM('job_seeker', 'employer', 'admin') NOT NULL,
    full_name VARCHAR(100) NOT NULL,
    phone VARCHAR(20),
    address TEXT,
    is_active BOOLEAN DEFAULT TRUE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

-- Jobs table
CREATE TABLE IF NOT EXISTS jobs (
    id INT AUTO_INCREMENT PRIMARY KEY,
    title VARCHAR(200) NOT NULL,
    company_name VARCHAR(200) NOT NULL,
    location VARCHAR(200) NOT NULL,
    salary DECIMAL(10,2),
    description TEXT NOT NULL,
    requirements TEXT,
    employer_id INT NOT NULL,
    status ENUM('open', 'closed', 'removed') DEFAULT 'open',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (employer_id) REFERENCES users(id) ON DELETE CASCADE
);

-- Job applications table
CREATE TABLE IF NOT EXISTS job_applications (
    id INT AUTO_INCREMENT PRIMARY KEY,
    job_id INT NOT NULL,
    job_seeker_id INT NOT NULL,
    application_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    status ENUM('pending', 'reviewed', 'accepted', 'rejected') DEFAULT 'pending',
    cover_letter TEXT,
    FOREIGN KEY (job_id) REFERENCES jobs(id) ON DELETE CASCADE,
    FOREIGN KEY (job_seeker_id) REFERENCES users(id) ON DELETE CASCADE,
    UNIQUE KEY unique_application (job_id, job_seeker_id)
);

-- Insert sample admin user
INSERT INTO users (username, password, email, role, full_name, phone, address, is_active)
VALUES ('admin', 'admin123', 'admin@jobportal.com', 'admin', 'System Administrator', '123-456-7890', 'Admin Address', TRUE);

-- Insert sample employer
INSERT INTO users (username, password, email, role, full_name, phone, address, is_active)
VALUES ('employer1', 'employer123', 'employer@company.com', 'employer', 'John Employer', '987-654-3210', 'Company Address', TRUE);

-- Insert sample job seeker
INSERT INTO users (username, password, email, role, full_name, phone, address, is_active)
VALUES ('seeker1', 'seeker123', 'seeker@email.com', 'job_seeker', 'Jane Seeker', '555-123-4567', 'Seeker Address', TRUE);

-- Insert sample job
INSERT INTO jobs (title, company_name, location, salary, description, requirements, employer_id, status)
VALUES ('Java Developer', 'Tech Solutions Inc', 'New York, NY', 80000.00, 
        'We are looking for a skilled Java developer to join our team.', 
        '3+ years Java experience, Spring Framework, MySQL', 2, 'open');

-- Show tables
SHOW TABLES;
