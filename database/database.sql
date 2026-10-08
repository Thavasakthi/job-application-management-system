-- ========================================================
-- Job Application Management System Database Schema
-- Database Engine: MySQL 8.0+
-- ========================================================

CREATE DATABASE IF NOT EXISTS job_portal_db;
USE job_portal_db;

-- --------------------------------------------------------
-- Table: users
-- --------------------------------------------------------
DROP TABLE IF EXISTS applications;
DROP TABLE IF EXISTS jobs;
DROP TABLE IF EXISTS users;

CREATE TABLE users (
    id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    email VARCHAR(100) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    role VARCHAR(20) NOT NULL CHECK (role IN ('APPLICANT', 'RECRUITER', 'ADMIN')),
    phone VARCHAR(20),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- --------------------------------------------------------
-- Table: jobs
-- --------------------------------------------------------
CREATE TABLE jobs (
    id INT AUTO_INCREMENT PRIMARY KEY,
    title VARCHAR(150) NOT NULL,
    company VARCHAR(100) NOT NULL,
    location VARCHAR(100) NOT NULL,
    salary DOUBLE NOT NULL,
    experience_years INT NOT NULL DEFAULT 0,
    job_type VARCHAR(30) NOT NULL CHECK (job_type IN ('FULL_TIME', 'PART_TIME', 'REMOTE', 'INTERNSHIP', 'CONTRACT')),
    description TEXT,
    recruiter_id INT NOT NULL,
    is_active BOOLEAN DEFAULT TRUE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (recruiter_id) REFERENCES users(id) ON DELETE CASCADE
);

-- --------------------------------------------------------
-- Table: applications
-- --------------------------------------------------------
CREATE TABLE applications (
    id INT AUTO_INCREMENT PRIMARY KEY,
    job_id INT NOT NULL,
    applicant_id INT NOT NULL,
    cover_letter TEXT,
    status VARCHAR(30) NOT NULL DEFAULT 'APPLIED' CHECK (status IN ('APPLIED', 'UNDER_REVIEW', 'SHORTLISTED', 'INTERVIEW_SCHEDULED', 'ACCEPTED', 'REJECTED', 'WITHDRAWN')),
    applied_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (job_id) REFERENCES jobs(id) ON DELETE CASCADE,
    FOREIGN KEY (applicant_id) REFERENCES users(id) ON DELETE CASCADE,
    CONSTRAINT unique_job_applicant UNIQUE (job_id, applicant_id)
);

-- --------------------------------------------------------
-- Indexes for Performance Tuning
-- --------------------------------------------------------
CREATE INDEX idx_job_title ON jobs(title);
CREATE INDEX idx_job_company ON jobs(company);
CREATE INDEX idx_job_active ON jobs(is_active);
CREATE INDEX idx_app_user ON applications(applicant_id);
CREATE INDEX idx_app_job ON applications(job_id);

-- --------------------------------------------------------
-- Sample Seed Data
-- --------------------------------------------------------

-- Insert Users (Password stored as plain text / hashed in real app)
INSERT INTO users (name, email, password, role, phone) VALUES
('System Administrator', 'admin@jobportal.com', 'admin123', 'ADMIN', '9876543210'),
('Sarah Connor (Tech Recruiter)', 'sarah@google.com', 'recruiter123', 'RECRUITER', '9876543211'),
('Robert Downey (HR Lead)', 'robert@microsoft.com', 'recruiter123', 'RECRUITER', '9876543212'),
('John Doe (Software Dev)', 'john.doe@email.com', 'user123', 'APPLICANT', '9876543213'),
('Jane Smith (Full Stack Dev)', 'jane.smith@email.com', 'user123', 'APPLICANT', '9876543214'),
('Tamil Selvan (Graduate Engineer)', 'tamil@email.com', 'user123', 'APPLICANT', '9876543215');

-- Insert Jobs
INSERT INTO jobs (title, company, location, salary, experience_years, job_type, description, recruiter_id, is_active) VALUES
('Senior Java Developer', 'Google', 'Mountain View, CA', 140000.0, 5, 'FULL_TIME', 'Design and implement scalable backend services using Java, Spring Boot, and Cloud microservices.', 2, TRUE),
('Full Stack Software Engineer', 'Microsoft', 'Redmond, WA', 125000.0, 3, 'REMOTE', 'Develop rich web applications using Java, React, and Azure Cloud solutions.', 3, TRUE),
('Java Backend Intern', 'Google', 'San Jose, CA', 60000.0, 0, 'INTERNSHIP', 'Join our core engineering team to build scalable APIs and database routines in Java.', 2, TRUE),
('Cloud Solutions Architect', 'Amazon Web Services', 'Seattle, WA', 155000.0, 7, 'FULL_TIME', 'Architect distributed systems and high-throughput databases on AWS infrastructure.', 3, TRUE),
('Associate Software Engineer', 'Zoho Corporation', 'Chennai, India', 55000.0, 1, 'FULL_TIME', 'Develop core SaaS tools using Java, JDBC, MySQL, and modern JavaScript.', 2, TRUE),
('DevOps Engineer', 'TCS', 'Bengaluru, India', 85000.0, 4, 'REMOTE', 'Manage CI/CD pipelines, Docker, Kubernetes, and automated deployment scripts.', 3, TRUE);

-- Insert Applications
INSERT INTO applications (job_id, applicant_id, cover_letter, status) VALUES
(1, 4, 'I have 5 years of backend engineering experience with Spring Boot and Java 17.', 'SHORTLISTED'),
(2, 5, 'Passionate Full Stack engineer with expert knowledge of Java, REST APIs, and React.', 'UNDER_REVIEW'),
(3, 6, 'Enthusiastic fresh graduate eager to learn core Java internal structures and JDBC.', 'APPLIED'),
(5, 6, 'Strong foundation in Java Data Structures, MySQL queries, and Object Oriented System Design.', 'INTERVIEW_SCHEDULED');

SELECT 'Database and Sample Data Initialized Successfully!' AS Status;
