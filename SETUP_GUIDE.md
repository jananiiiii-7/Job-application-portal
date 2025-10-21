# Job Portal Application - Setup Guide

## Prerequisites

### 1. Install MySQL Server
Download and install MySQL Community Server from:
https://dev.mysql.com/downloads/mysql/

During installation:
- Remember the root password you set
- Note the installation directory (default: C:\Program Files\MySQL\MySQL Server 8.0\)
- Add MySQL to your system PATH during installation

### 2. Install MySQL Workbench (Optional but recommended)
Download from: https://dev.mysql.com/downloads/workbench/

### 3. Install Java Development Kit (JDK)
Download JDK 8 or higher from: https://www.oracle.com/java/technologies/javase-downloads.html

### 4. Download MySQL Connector/J
Download from: https://dev.mysql.com/downloads/connector/j/
- Extract the JAR file (e.g., mysql-connector-java-8.0.xx.jar)

## Database Setup

### Option 1: Using MySQL Command Line
1. Open Command Prompt as Administrator
2. Navigate to MySQL bin directory:
   ```cmd
   cd "C:\Program Files\MySQL\MySQL Server 8.0\bin"
   ```
3. Connect to MySQL:
   ```cmd
   mysql -u root -p
   ```
4. Enter your root password when prompted
5. Run the SQL commands:

```sql
-- Create database
CREATE DATABASE job_portal_db;
USE job_portal_db;

-- Create users table
CREATE TABLE users (
    id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    email VARCHAR(100) UNIQUE NOT NULL,
    password VARCHAR(100) NOT NULL,
    role ENUM('job_seeker', 'employer', 'admin') NOT NULL,
    status ENUM('active', 'inactive') DEFAULT 'active'
);

-- Create jobs table
CREATE TABLE jobs (
    id INT AUTO_INCREMENT PRIMARY KEY,
    employer_id INT NOT NULL,
    title VARCHAR(200) NOT NULL,
    company_name VARCHAR(200),
    location VARCHAR(100),
    salary DECIMAL(10,2),
    description TEXT,
    status ENUM('active', 'closed') DEFAULT 'active',
    FOREIGN KEY (employer_id) REFERENCES users(id)
);

-- Create job_applications table
CREATE TABLE job_applications (
    id INT AUTO_INCREMENT PRIMARY KEY,
    user_id INT NOT NULL,
    job_id INT NOT NULL,
    application_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    status ENUM('pending', 'accepted', 'rejected') DEFAULT 'pending',
    FOREIGN KEY (user_id) REFERENCES users(id),
    FOREIGN KEY (job_id) REFERENCES jobs(id)
);

-- Insert sample data
INSERT INTO users (name, email, password, role) VALUES 
('Admin User', 'admin@jobportal.com', 'admin123', 'admin'),
('Tech Company', 'employer@tech.com', 'employer123', 'employer'),
('John Doe', 'john@email.com', 'jobseeker123', 'job_seeker');

-- Exit MySQL
EXIT;
```

### Option 2: Using MySQL Workbench
1. Open MySQL Workbench
2. Connect to your MySQL server
3. Create a new SQL tab
4. Copy and execute the SQL commands above
5. Click the execute button (lightning bolt icon)

## Application Configuration

### 1. Update Database Connection
Edit `src/com/jobportalapp/db/DBConnection.java`:
```java
private static final String URL = "jdbc:mysql://localhost:3306/job_portal_db";
private static final String USER = "root"; // Your MySQL username
private static final String PASSWORD = "your_password_here"; // Your MySQL password
```

### 2. Add MySQL Connector to Classpath
Place the MySQL Connector/J JAR file in a `lib` folder in your project directory.

## Compile and Run

### Using Command Line:
```cmd
# Navigate to project directory
cd c:\JobPortalApp-Java-Swing-MySQL

# Compile (replace with your actual JAR filename)
javac -cp ".;lib/mysql-connector-java-8.0.xx.jar" src/com/jobportalapp/**/*.java -d bin/

# Run
java -cp ".;bin;lib/mysql-connector-java-8.0.xx.jar" com.jobportalapp.ui.LoginForm
```

### Using IDE (Eclipse/IntelliJ):
1. Import the project
2. Add MySQL Connector/J to build path
3. Set main class to `com.jobportalapp.ui.LoginForm`
4. Run the project

## Test Login Credentials
- Admin: admin@jobportal.com / admin123
- Employer: employer@tech.com / employer123
- Job Seeker: john@email.com / jobseeker123

## Troubleshooting
- Ensure MySQL service is running (check Services panel)
- Verify database credentials in DBConnection.java
- Check that MySQL Connector/J is in classpath
