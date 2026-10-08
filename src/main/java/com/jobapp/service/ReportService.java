package com.jobapp.service;

import com.jobapp.dao.ApplicationDAO;
import com.jobapp.dao.DAOFactory;
import com.jobapp.dao.JobDAO;
import com.jobapp.exception.DatabaseException;
import com.jobapp.model.Application;
import com.jobapp.model.Job;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

public class ReportService {
    private final JobDAO jobDAO;
    private final ApplicationDAO applicationDAO;

    public ReportService() {
        this.jobDAO = DAOFactory.getJobDAO();
        this.applicationDAO = DAOFactory.getApplicationDAO();
    }

    public ReportService(JobDAO jobDAO, ApplicationDAO applicationDAO) {
        this.jobDAO = jobDAO;
        this.applicationDAO = applicationDAO;
    }

    public String exportJobsToCSV(String filePath) throws DatabaseException, IOException {
        List<Job> jobs = jobDAO.findAll();
        File file = new File(filePath);

        try (PrintWriter writer = new PrintWriter(new FileWriter(file))) {
            writer.println("Job ID,Title,Company,Location,Salary,Experience (Yrs),Job Type,Status");
            for (Job job : jobs) {
                writer.printf("%d,\"%s\",\"%s\",\"%s\",%.2f,%d,%s,%s%n",
                        job.getId(),
                        escapeCSV(job.getTitle()),
                        escapeCSV(job.getCompany()),
                        escapeCSV(job.getLocation()),
                        job.getSalary(),
                        job.getExperienceYears(),
                        job.getJobType().getDisplayName(),
                        job.isActive() ? "Active" : "Closed");
            }
        }
        return file.getAbsolutePath();
    }

    public String exportApplicantHistoryToCSV(int applicantId, String filePath) throws DatabaseException, IOException {
        List<Application> apps = applicationDAO.findByApplicantId(applicantId);
        File file = new File(filePath);

        try (PrintWriter writer = new PrintWriter(new FileWriter(file))) {
            writer.println("Application ID,Job ID,Job Title,Company,Status,Applied Date");
            for (Application app : apps) {
                writer.printf("%d,%d,\"%s\",\"%s\",%s,\"%s\"%n",
                        app.getId(),
                        app.getJobId(),
                        escapeCSV(app.getJobTitle() != null ? app.getJobTitle() : "N/A"),
                        escapeCSV(app.getCompany() != null ? app.getCompany() : "N/A"),
                        app.getStatus().getDescription(),
                        app.getAppliedAt());
            }
        }
        return file.getAbsolutePath();
    }

    public String generateSystemSummaryReport() throws DatabaseException {
        List<Job> jobs = jobDAO.findAll();
        List<Job> activeJobs = jobDAO.findActiveJobs();
        int totalApps = applicationDAO.countTotalApplications();

        StringBuilder sb = new StringBuilder();
        sb.append("=========================================\n");
        sb.append("      SYSTEM METRICS & SUMMARY REPORT    \n");
        sb.append("=========================================\n");
        sb.append(String.format("Total Jobs Posted : %d%n", jobs.size()));
        sb.append(String.format("Active Postings   : %d%n", activeJobs.size()));
        sb.append(String.format("Closed Postings   : %d%n", jobs.size() - activeJobs.size()));
        sb.append(String.format("Total Applications: %d%n", totalApps));
        sb.append("-----------------------------------------\n");

        return sb.toString();
    }

    private String escapeCSV(String data) {
        if (data == null) return "";
        return data.replace("\"", "\"\"");
    }
}
