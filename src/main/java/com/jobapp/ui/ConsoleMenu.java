package com.jobapp.ui;

import com.jobapp.config.DatabaseConfig;
import com.jobapp.exception.*;
import com.jobapp.model.*;
import com.jobapp.service.*;
import com.jobapp.util.ConsoleUtil;
import com.jobapp.util.InputValidator;

import java.io.IOException;
import java.util.List;

public class ConsoleMenu {
    private final UserService userService;
    private final JobService jobService;
    private final ApplicationService applicationService;
    private final ReportService reportService;
    private User currentUser;

    public ConsoleMenu() {
        this.userService = new UserService();
        this.jobService = new JobService();
        this.applicationService = new ApplicationService();
        this.reportService = new ReportService();
    }

    public void start() {
        ConsoleUtil.printBanner();

        if (DatabaseConfig.isDatabaseAvailable()) {
            ConsoleUtil.printSuccess("Connected to MySQL Database via JDBC.");
        } else {
            ConsoleUtil.printInfo("Running in In-Memory Storage Mode with pre-populated demo data.");
        }

        boolean running = true;
        while (running) {
            if (currentUser == null) {
                showGuestMenu();
            } else {
                switch (currentUser.getRole()) {
                    case APPLICANT:
                        showApplicantMenu();
                        break;
                    case RECRUITER:
                        showRecruiterMenu();
                        break;
                    case ADMIN:
                        showAdminMenu();
                        break;
                }
            }
        }
    }

    // ---------------------------------------------------------
    // GUEST / MAIN MENU
    // ---------------------------------------------------------
    private void showGuestMenu() {
        ConsoleUtil.printHeader("Welcome Menu");
        System.out.println("1. Login");
        System.out.println("2. Register New User Account");
        System.out.println("3. View All Active Jobs");
        System.out.println("4. Search Jobs by Title or Company");
        System.out.println("5. Filter Jobs by Location / Type / Salary");
        System.out.println("6. System Summary & Metrics");
        System.out.println("0. Exit System");
        System.out.println("----------------------------------------------------------");

        int choice = InputValidator.readInt("Select an option: ");
        try {
            switch (choice) {
                case 1:
                    handleLogin();
                    break;
                case 2:
                    handleRegister();
                    break;
                case 3:
                    viewAllJobs();
                    break;
                case 4:
                    searchJobs();
                    break;
                case 5:
                    filterJobs();
                    break;
                case 6:
                    showSystemSummary();
                    break;
                case 0:
                    ConsoleUtil.printSuccess("Thank you for using Job Application Management System. Goodbye!");
                    System.exit(0);
                    break;
                default:
                    ConsoleUtil.printError("Invalid option! Please enter a number between 0 and 6.");
            }
        } catch (JobAppException e) {
            ConsoleUtil.printError("Error: " + e.getMessage());
        }
    }

    // ---------------------------------------------------------
    // APPLICANT MENU
    // ---------------------------------------------------------
    private void showApplicantMenu() {
        ConsoleUtil.printHeader("Applicant Dashboard - Welcome, " + currentUser.getName());
        System.out.println("1. View All Active Jobs");
        System.out.println("2. Search Jobs by Title or Company");
        System.out.println("3. View Job Details by ID");
        System.out.println("4. Apply for a Job");
        System.out.println("5. View Applied Jobs & Application Status");
        System.out.println("6. Withdraw an Application");
        System.out.println("7. Export My Applications to CSV");
        System.out.println("8. Logout");
        System.out.println("0. Exit System");
        System.out.println("----------------------------------------------------------");

        int choice = InputValidator.readInt("Select an option: ");
        try {
            switch (choice) {
                case 1:
                    viewAllJobs();
                    break;
                case 2:
                    searchJobs();
                    break;
                case 3:
                    viewJobDetails();
                    break;
                case 4:
                    applyForJob();
                    break;
                case 5:
                    viewAppliedJobs();
                    break;
                case 6:
                    withdrawApplication();
                    break;
                case 7:
                    exportApplicantHistory();
                    break;
                case 8:
                    handleLogout();
                    break;
                case 0:
                    ConsoleUtil.printSuccess("Goodbye, " + currentUser.getName() + "!");
                    System.exit(0);
                    break;
                default:
                    ConsoleUtil.printError("Invalid choice! Select between 0 and 8.");
            }
        } catch (JobAppException e) {
            ConsoleUtil.printError("Error: " + e.getMessage());
        }
    }

    // ---------------------------------------------------------
    // RECRUITER MENU
    // ---------------------------------------------------------
    private void showRecruiterMenu() {
        ConsoleUtil.printHeader("Recruiter Dashboard - Welcome, " + currentUser.getName());
        System.out.println("1. Add New Job Details");
        System.out.println("2. View My Job Postings");
        System.out.println("3. View Job Details by ID");
        System.out.println("4. Search Jobs by Title or Company");
        System.out.println("5. View Applicants / Applications Received");
        System.out.println("6. Track & Update Application Status");
        System.out.println("7. Export All Jobs to CSV");
        System.out.println("8. Close / Activate Job Posting");
        System.out.println("9. Logout");
        System.out.println("0. Exit System");
        System.out.println("----------------------------------------------------------");

        int choice = InputValidator.readInt("Select an option: ");
        try {
            switch (choice) {
                case 1:
                    addNewJob();
                    break;
                case 2:
                    viewMyPostedJobs();
                    break;
                case 3:
                    viewJobDetails();
                    break;
                case 4:
                    searchJobs();
                    break;
                case 5:
                    viewApplicationsReceived();
                    break;
                case 6:
                    updateApplicationStatus();
                    break;
                case 7:
                    exportJobsCSV();
                    break;
                case 8:
                    toggleJobActiveStatus();
                    break;
                case 9:
                    handleLogout();
                    break;
                case 0:
                    ConsoleUtil.printSuccess("Goodbye, " + currentUser.getName() + "!");
                    System.exit(0);
                    break;
                default:
                    ConsoleUtil.printError("Invalid choice! Select between 0 and 9.");
            }
        } catch (JobAppException e) {
            ConsoleUtil.printError("Error: " + e.getMessage());
        }
    }

    // ---------------------------------------------------------
    // ADMIN MENU
    // ---------------------------------------------------------
    private void showAdminMenu() {
        ConsoleUtil.printHeader("Admin Dashboard - Welcome, " + currentUser.getName());
        System.out.println("1. Add New Job Details");
        System.out.println("2. View All Jobs (Active & Closed)");
        System.out.println("3. Search Jobs by Title or Company");
        System.out.println("4. View All Registered Users");
        System.out.println("5. Track & Update Application Status");
        System.out.println("6. Export Jobs to CSV");
        System.out.println("7. System Metrics & Statistics");
        System.out.println("8. Logout");
        System.out.println("0. Exit System");
        System.out.println("----------------------------------------------------------");

        int choice = InputValidator.readInt("Select an option: ");
        try {
            switch (choice) {
                case 1:
                    addNewJob();
                    break;
                case 2:
                    viewAllJobsAdmin();
                    break;
                case 3:
                    searchJobs();
                    break;
                case 4:
                    viewAllUsers();
                    break;
                case 5:
                    updateApplicationStatus();
                    break;
                case 6:
                    exportJobsCSV();
                    break;
                case 7:
                    showSystemSummary();
                    break;
                case 8:
                    handleLogout();
                    break;
                case 0:
                    ConsoleUtil.printSuccess("Goodbye, Admin!");
                    System.exit(0);
                    break;
                default:
                    ConsoleUtil.printError("Invalid choice! Select between 0 and 8.");
            }
        } catch (JobAppException e) {
            ConsoleUtil.printError("Error: " + e.getMessage());
        }
    }

    // ---------------------------------------------------------
    // ACTION HANDLERS
    // ---------------------------------------------------------

    private void handleLogin() throws JobAppException {
        ConsoleUtil.printHeader("User Login");
        String email = InputValidator.readString("Enter Email: ");
        String password = InputValidator.readString("Enter Password: ");

        this.currentUser = userService.login(email, password);
        ConsoleUtil.printSuccess("Login successful! Logged in as: " + currentUser.getName() + " (" + currentUser.getRole() + ")");
    }

    private void handleRegister() throws JobAppException {
        ConsoleUtil.printHeader("User Registration");
        String name = InputValidator.readString("Full Name: ");
        String email = InputValidator.readString("Email Address: ");
        String password = InputValidator.readString("Password: ");
        String phone = InputValidator.readString("Phone Number: ");

        System.out.println("Account Role Options:");
        System.out.println("1. APPLICANT (Job Seeker)");
        System.out.println("2. RECRUITER (Employer / Hiring Manager)");
        int roleChoice = InputValidator.readInt("Select Role (1 or 2): ");
        Role role = (roleChoice == 2) ? Role.RECRUITER : Role.APPLICANT;

        User newUser = userService.registerUser(name, email, password, role, phone);
        ConsoleUtil.printSuccess("Registration successful! Account ID: " + newUser.getId() + ". You may now log in.");
    }

    private void handleLogout() {
        if (currentUser != null) {
            ConsoleUtil.printInfo("User " + currentUser.getName() + " logged out.");
            currentUser = null;
        }
    }

    private void addNewJob() throws JobAppException {
        ConsoleUtil.printHeader("Add New Job Posting");
        String title = InputValidator.readString("Job Title: ");
        String company = InputValidator.readString("Company Name: ");
        String location = InputValidator.readString("Location (e.g. Remote, Mountain View, CA): ");
        double salary = InputValidator.readDouble("Annual Salary ($): ");
        int exp = InputValidator.readInt("Required Experience (years): ");

        System.out.println("Job Type Options:");
        System.out.println("1. FULL_TIME  2. PART_TIME  3. REMOTE  4. INTERNSHIP  5. CONTRACT");
        int typeChoice = InputValidator.readInt("Select Job Type (1-5): ");
        JobType jobType;
        switch (typeChoice) {
            case 2: jobType = JobType.PART_TIME; break;
            case 3: jobType = JobType.REMOTE; break;
            case 4: jobType = JobType.INTERNSHIP; break;
            case 5: jobType = JobType.CONTRACT; break;
            default: jobType = JobType.FULL_TIME; break;
        }

        String description = InputValidator.readString("Job Description / Requirements: ");
        int recruiterId = (currentUser != null) ? currentUser.getId() : 1;

        Job createdJob = jobService.addJob(title, company, location, salary, exp, jobType, description, recruiterId);
        ConsoleUtil.printSuccess("Job posting created successfully! Job ID: " + createdJob.getId());
    }

    private void viewAllJobs() throws DatabaseException {
        ConsoleUtil.printHeader("Active Job Openings");
        List<Job> jobs = jobService.getAllActiveJobs();
        ConsoleUtil.printJobsTable(jobs);
    }

    private void viewAllJobsAdmin() throws DatabaseException {
        ConsoleUtil.printHeader("All Job Postings (Admin View)");
        List<Job> jobs = jobService.getJobsByRecruiter(0); // get all if recruiterId 0 or fallback
        if (jobs.isEmpty()) {
            jobs = jobService.getAllActiveJobs();
        }
        ConsoleUtil.printJobsTable(jobs);
    }

    private void viewMyPostedJobs() throws DatabaseException {
        ConsoleUtil.printHeader("My Posted Jobs");
        List<Job> jobs = jobService.getJobsByRecruiter(currentUser.getId());
        ConsoleUtil.printJobsTable(jobs);
    }

    private void searchJobs() throws DatabaseException {
        ConsoleUtil.printHeader("Search Jobs");
        String keyword = InputValidator.readString("Enter title or company keyword to search: ");
        List<Job> jobs = jobService.searchJobs(keyword);
        ConsoleUtil.printJobsTable(jobs);
    }

    private void filterJobs() throws DatabaseException {
        ConsoleUtil.printHeader("Filter Jobs");
        String location = InputValidator.readString("Location (leave empty to ignore): ");
        
        System.out.println("Job Type (0: All, 1: FULL_TIME, 2: PART_TIME, 3: REMOTE, 4: INTERNSHIP): ");
        int typeChoice = InputValidator.readInt("Choice: ");
        JobType jobType = null;
        if (typeChoice == 1) jobType = JobType.FULL_TIME;
        else if (typeChoice == 2) jobType = JobType.PART_TIME;
        else if (typeChoice == 3) jobType = JobType.REMOTE;
        else if (typeChoice == 4) jobType = JobType.INTERNSHIP;

        double minSalary = InputValidator.readDouble("Minimum Salary ($) (0 to ignore): ");

        List<Job> jobs = jobService.filterJobs(location, jobType, minSalary);
        ConsoleUtil.printJobsTable(jobs);
    }

    private void viewJobDetails() throws JobAppException {
        int jobId = InputValidator.readInt("Enter Job ID to view details: ");
        Job job = jobService.getJobById(jobId);
        ConsoleUtil.printJobDetail(job);
    }

    private void applyForJob() throws JobAppException {
        if (currentUser == null || currentUser.getRole() != Role.APPLICANT) {
            ConsoleUtil.printError("You must be logged in as an Applicant to apply for jobs!");
            return;
        }

        ConsoleUtil.printHeader("Apply for a Job");
        int jobId = InputValidator.readInt("Enter Job ID to Apply: ");
        Job job = jobService.getJobById(jobId);
        ConsoleUtil.printJobDetail(job);

        boolean confirm = InputValidator.readBoolean("Confirm submitting application for '" + job.getTitle() + "' at " + job.getCompany() + "?");
        if (!confirm) {
            ConsoleUtil.printInfo("Application cancelled.");
            return;
        }

        String coverLetter = InputValidator.readString("Enter Cover Letter / Application Note: ");
        Application app = applicationService.applyForJob(jobId, currentUser.getId(), coverLetter);
        ConsoleUtil.printSuccess("Application submitted successfully! Application ID: " + app.getId() + " | Status: " + app.getStatus().getDescription());
    }

    private void viewAppliedJobs() throws DatabaseException {
        if (currentUser == null) return;
        ConsoleUtil.printHeader("Applied Jobs & Application Status");
        List<Application> apps = applicationService.getApplicantApplications(currentUser.getId());
        ConsoleUtil.printApplicationsTable(apps);
    }

    private void withdrawApplication() throws JobAppException {
        if (currentUser == null) return;
        viewAppliedJobs();
        int appId = InputValidator.readInt("Enter Application ID to withdraw: ");
        boolean confirmed = InputValidator.readBoolean("Are you sure you want to withdraw application ID " + appId + "?");
        if (confirmed) {
            boolean success = applicationService.withdrawApplication(appId, currentUser.getId());
            if (success) {
                ConsoleUtil.printSuccess("Application ID " + appId + " has been successfully withdrawn.");
            } else {
                ConsoleUtil.printError("Failed to withdraw application.");
            }
        }
    }

    private void viewApplicationsReceived() throws DatabaseException {
        ConsoleUtil.printHeader("Applications Received for My Job Postings");
        List<Application> apps = applicationService.getApplicationsForRecruiter(currentUser.getId());
        ConsoleUtil.printApplicationsTable(apps);
    }

    private void updateApplicationStatus() throws JobAppException {
        ConsoleUtil.printHeader("Update Application Status");
        int appId = InputValidator.readInt("Enter Application ID: ");
        Application app = applicationService.getApplicationById(appId);

        System.out.println("Current Status: " + app.getStatus().getDescription());
        System.out.println("Select New Status:");
        System.out.println("1. APPLIED");
        System.out.println("2. UNDER_REVIEW");
        System.out.println("3. SHORTLISTED");
        System.out.println("4. INTERVIEW_SCHEDULED");
        System.out.println("5. ACCEPTED");
        System.out.println("6. REJECTED");

        int choice = InputValidator.readInt("Choice (1-6): ");
        ApplicationStatus newStatus;
        switch (choice) {
            case 1: newStatus = ApplicationStatus.APPLIED; break;
            case 2: newStatus = ApplicationStatus.UNDER_REVIEW; break;
            case 3: newStatus = ApplicationStatus.SHORTLISTED; break;
            case 4: newStatus = ApplicationStatus.INTERVIEW_SCHEDULED; break;
            case 5: newStatus = ApplicationStatus.ACCEPTED; break;
            case 6: newStatus = ApplicationStatus.REJECTED; break;
            default:
                ConsoleUtil.printError("Invalid selection.");
                return;
        }

        boolean updated = applicationService.updateApplicationStatus(appId, newStatus);
        if (updated) {
            ConsoleUtil.printSuccess("Application ID " + appId + " status updated to: " + newStatus.getDescription());
        } else {
            ConsoleUtil.printError("Failed to update status.");
        }
    }

    private void toggleJobActiveStatus() throws JobAppException {
        int jobId = InputValidator.readInt("Enter Job ID to toggle status: ");
        Job job = jobService.getJobById(jobId);
        boolean newActive = !job.isActive();
        jobService.updateJobStatus(jobId, newActive);
        ConsoleUtil.printSuccess("Job ID " + jobId + " status updated to " + (newActive ? "ACTIVE" : "CLOSED"));
    }

    private void viewAllUsers() throws DatabaseException {
        ConsoleUtil.printHeader("Registered Users List");
        List<User> users = userService.getAllUsers();
        String format = "| %-5s | %-25s | %-30s | %-12s | %-15s |%n";
        System.out.println(ConsoleUtil.CYAN + "+-------+---------------------------+--------------------------------+--------------+-----------------+" + ConsoleUtil.RESET);
        System.out.printf(ConsoleUtil.BOLD + format + ConsoleUtil.RESET, "ID", "Name", "Email", "Role", "Phone");
        System.out.println(ConsoleUtil.CYAN + "+-------+---------------------------+--------------------------------+--------------+-----------------+" + ConsoleUtil.RESET);

        for (User u : users) {
            System.out.printf(format, u.getId(), u.getName(), u.getEmail(), u.getRole().name(), u.getPhone());
        }
        System.out.println(ConsoleUtil.CYAN + "+-------+---------------------------+--------------------------------+--------------+-----------------+" + ConsoleUtil.RESET);
    }

    private void exportJobsCSV() throws DatabaseException {
        try {
            String path = reportService.exportJobsToCSV("jobs_export.csv");
            ConsoleUtil.printSuccess("Jobs exported successfully to CSV: " + path);
        } catch (IOException e) {
            ConsoleUtil.printError("Export failed: " + e.getMessage());
        }
    }

    private void exportApplicantHistory() throws DatabaseException {
        if (currentUser == null) return;
        try {
            String path = reportService.exportApplicantHistoryToCSV(currentUser.getId(), "my_applications.csv");
            ConsoleUtil.printSuccess("Applications exported successfully to CSV: " + path);
        } catch (IOException e) {
            ConsoleUtil.printError("Export failed: " + e.getMessage());
        }
    }

    private void showSystemSummary() throws DatabaseException {
        String report = reportService.generateSystemSummaryReport();
        System.out.println(ConsoleUtil.YELLOW + report + ConsoleUtil.RESET);
    }
}
