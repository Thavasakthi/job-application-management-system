# 🚀 Job Application Management System (Core Java & MySQL JDBC)

A complete, production-grade **Job Application Management System** built with **Core Java (JDK 17+)**, **MySQL JDBC**, **Collections Framework**, **Custom Exception Handling**, and a clean **Layered Architecture**.

---

## 🌟 Key Features

### 🏢 Job Management
- **Add Job Details**: Employers / Recruiters can add job postings with title, company, location, salary, experience requirement, job type, and detailed description.
- **View Job Details**: View complete details for any job posting by Job ID.
- **Search Jobs by Title or Company**: Case-insensitive search across job titles and company names.
- **Filter Jobs**: Filter postings by Location, Job Type (*Full-Time, Part-Time, Remote, Internship, Contract*), and Minimum Salary.
- **Manage Job Status**: Toggle job postings between `ACTIVE` and `CLOSED`.

### 👨‍💻 Application Management
- **Apply for Jobs**: Job seekers (Applicants) can browse open positions, submit applications, and add cover letters.
- **View Applied Jobs**: Applicants can track all their submitted applications in real-time.
- **Track Application Status**: Full status lifecycle tracking:
  `APPLIED` ➔ `UNDER_REVIEW` ➔ `SHORTLISTED` ➔ `INTERVIEW_SCHEDULED` ➔ `ACCEPTED` / `REJECTED` / `WITHDRAWN`.
- **Withdraw Application**: Applicants can withdraw pending applications.
- **Recruiter Dashboard**: Recruiters can view all applications submitted for their specific job listings and update application statuses.

### 📊 Reporting & File Exporting
- **CSV Export**: Export all jobs or applicant history directly to `.csv` files.
- **System Metrics & Summary**: High-level analytical overview of total jobs, active vs closed postings, and total applications.

### 🛡️ Core Java Architectural Highlights
- **Object-Oriented Programming (OOP)**: Clean encapsulation, abstraction, inheritance, and interfaces.
- **Design Patterns**:
  - **DAO Pattern (Data Access Object)**: Decouples business logic from persistence storage.
  - **Factory Pattern (`DAOFactory`)**: Dynamically switches between **MySQL JDBC DAOs** and **In-Memory Storage DAOs**.
- **Dual-Storage Engine**: Runs out-of-the-box using In-Memory Collection storage if MySQL is not running locally, while providing full production JDBC MySQL driver integration when connected!
- **Collections & Streams**: Uses `List`, `Map`, `Set`, `Optional`, and Java 8+ Stream API for search, filtering, and reporting.
- **Custom Exception Hierarchy**: `JobAppException`, `DatabaseException`, `JobNotFoundException`, `UserNotFoundException`, `ApplicationException`, `InvalidInputException`.

---

## 📁 Project Directory Structure

```
├── database/
│   └── database.sql                   # MySQL DDL & Seed Data Script
├── src/
│   ├── main/
│   │   ├── java/com/jobapp/
│   │   │   ├── App.java               # Application Main Entry Point
│   │   │   ├── config/
│   │   │   │   └── DatabaseConfig.java # Database Driver & JDBC Connection Config
│   │   │   ├── dao/
│   │   │   │   ├── UserDAO.java
│   │   │   │   ├── UserDAOImpl.java   # MySQL JDBC Implementation
│   │   │   │   ├── UserDAOInMemoryImpl.java
│   │   │   │   ├── JobDAO.java
│   │   │   │   ├── JobDAOImpl.java    # MySQL JDBC Implementation
│   │   │   │   ├── JobDAOInMemoryImpl.java
│   │   │   │   ├── ApplicationDAO.java
│   │   │   │   ├── ApplicationDAOImpl.java
│   │   │   │   ├── ApplicationDAOInMemoryImpl.java
│   │   │   │   └── DAOFactory.java    # Dynamic DAO Selection Factory
│   │   │   ├── exception/
│   │   │   │   ├── JobAppException.java
│   │   │   │   ├── DatabaseException.java
│   │   │   │   ├── JobNotFoundException.java
│   │   │   │   ├── UserNotFoundException.java
│   │   │   │   ├── ApplicationException.java
│   │   │   │   └── InvalidInputException.java
│   │   │   ├── model/
│   │   │   │   ├── User.java
│   │   │   │   ├── Job.java
│   │   │   │   ├── Application.java
│   │   │   │   ├── Role.java
│   │   │   │   ├── JobType.java
│   │   │   │   └── ApplicationStatus.java
│   │   │   ├── service/
│   │   │   │   ├── UserService.java
│   │   │   │   ├── JobService.java
│   │   │   │   ├── ApplicationService.java
│   │   │   │   └── ReportService.java
│   │   │   ├── ui/
│   │   │   │   └── ConsoleMenu.java   # Menu-Driven Terminal UI
│   │   │   └── util/
│   │   │       ├── ConsoleUtil.java   # ANSI Formatting & Tabular Printing
│   │   │       └── InputValidator.java # Robust Terminal Input Utility
│   │   └── resources/
│   │       └── db.properties          # Database Configuration Properties
│   └── test/
│       └── java/com/jobapp/service/
│           ├── JobServiceTest.java    # JUnit 5 Unit Tests
│           ├── UserServiceTest.java
│           └── ApplicationServiceTest.java
├── pom.xml                            # Maven Dependencies & Build Config
└── README.md                          # Comprehensive Documentation
```

---

## 🗄️ Database Setup (MySQL)

1. Ensure MySQL Server is installed and running on `localhost:3306`.
2. Open MySQL CLI or Workbench and run the database initialization script:
   ```bash
   mysql -u root -p < database/database.sql
   ```
3. Update `src/main/resources/db.properties` with your MySQL credentials if needed:
   ```properties
   db.url=jdbc:mysql://localhost:3306/job_portal_db?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC
   db.user=root
   db.password=your_password
   ```

*Note: If MySQL is not running, the application automatically detects connection failure and gracefully falls back to In-Memory Collection Storage Mode with sample data.*

---

## 🛠️ How to Compile & Run

### 1. Build and Run Unit Tests
```bash
mvn clean test
```

### 2. Package the Application (Executable Shaded Jar)
```bash
mvn clean package
```

### 3. Execute Application
```bash
java -jar target/job-application-management-system-1.0.0.jar
```

Or run directly via Maven:
```bash
mvn exec:java -Dexec.mainClass="com.jobapp.App"
```

---

## 👥 Demo Pre-Populated Accounts

When running in default demo mode, you can log in using any of the following accounts:

| Role | Email | Password | Name |
| :--- | :--- | :--- | :--- |
| **APPLICANT** | `tamil@email.com` | `user123` | Tamil Selvan |
| **APPLICANT** | `john.doe@email.com` | `user123` | John Doe |
| **RECRUITER** | `sarah@google.com` | `recruiter123` | Sarah Connor (Google) |
| **ADMIN** | `admin@jobportal.com` | `admin123` | System Administrator |

---

## 💻 Sample Console Interface

```
==========================================================================================
           🚀 JOB APPLICATION MANAGEMENT SYSTEM (Core Java & JDBC MySQL)           
==========================================================================================
✔ Connected to MySQL Database via JDBC.

>>> WELCOME MENU <<<
------------------------------------------------------------------------------------------
1. Login
2. Register New User Account
3. View All Active Jobs
4. Search Jobs by Title or Company
5. Filter Jobs by Location / Type / Salary
6. System Summary & Metrics
0. Exit System
----------------------------------------------------------
Select an option: 3

>>> ACTIVE JOB OPENINGS <<<
------------------------------------------------------------------------------------------
+-------+---------------------------+--------------------+--------------------+--------------+------------+
| ID    | Title                     | Company            | Location           | Salary ($)   | Type       |
+-------+---------------------------+--------------------+--------------------+--------------+------------+
| 6     | DevOps Engineer           | TCS                | Bengaluru, India   | 85000.00     | Remote     |
| 5     | Associate Software Eng... | Zoho Corporation   | Chennai, India     | 55000.00     | Full-Time  |
| 4     | Cloud Solutions Archit... | Amazon Web Services| Seattle, WA        | 155000.00    | Full-Time  |
| 3     | Java Backend Intern       | Google             | San Jose, CA       | 60000.00     | Internship |
| 2     | Full Stack Software En... | Microsoft          | Redmond, WA        | 125000.00    | Remote     |
| 1     | Senior Java Developer     | Google             | Mountain View, CA  | 140000.00    | Full-Time  |
+-------+---------------------------+--------------------+--------------------+--------------+------------+
```

---

## 📄 License
This project is open-source and free to use under the **MIT License**.
