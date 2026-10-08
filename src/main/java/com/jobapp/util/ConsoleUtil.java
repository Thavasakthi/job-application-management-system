package com.jobapp.util;

import com.jobapp.model.Application;
import com.jobapp.model.Job;

import java.util.List;

public class ConsoleUtil {
    // ANSI Colors
    public static final String RESET = "\u001B[0m";
    public static final String BLACK = "\u001B[30m";
    public static final String RED = "\u001B[31m";
    public static final String GREEN = "\u001B[32m";
    public static final String YELLOW = "\u001B[33m";
    public static final String BLUE = "\u001B[34m";
    public static final String PURPLE = "\u001B[35m";
    public static final String CYAN = "\u001B[36m";
    public static final String WHITE = "\u001B[37m";
    public static final String BOLD = "\u001B[1m";

    public static void printBanner() {
        System.out.println(CYAN + BOLD + "==========================================================================================" + RESET);
        System.out.println(BLUE + BOLD + "           🚀 JOB APPLICATION MANAGEMENT SYSTEM (Core Java & JDBC MySQL)           " + RESET);
        System.out.println(CYAN + BOLD + "==========================================================================================" + RESET);
    }

    public static void printHeader(String title) {
        System.out.println();
        System.out.println(PURPLE + BOLD + ">>> " + title.toUpperCase() + " <<<" + RESET);
        System.out.println(CYAN + "------------------------------------------------------------------------------------------" + RESET);
    }

    public static void printSuccess(String message) {
        System.out.println(GREEN + BOLD + "✔ " + message + RESET);
    }

    public static void printError(String message) {
        System.out.println(RED + BOLD + "✖ " + message + RESET);
    }

    public static void printInfo(String message) {
        System.out.println(YELLOW + "ℹ " + message + RESET);
    }

    public static void printJobsTable(List<Job> jobs) {
        if (jobs == null || jobs.isEmpty()) {
            printInfo("No job postings found matching your criteria.");
            return;
        }

        String format = "| %-5s | %-25s | %-18s | %-18s | %-12s | %-10s |%n";
        System.out.println(CYAN + "+-------+---------------------------+--------------------+--------------------+--------------+------------+" + RESET);
        System.out.printf(BOLD + format + RESET, "ID", "Title", "Company", "Location", "Salary ($)", "Type");
        System.out.println(CYAN + "+-------+---------------------------+--------------------+--------------------+--------------+------------+" + RESET);

        for (Job j : jobs) {
            String title = j.getTitle().length() > 25 ? j.getTitle().substring(0, 22) + "..." : j.getTitle();
            String company = j.getCompany().length() > 18 ? j.getCompany().substring(0, 15) + "..." : j.getCompany();
            String loc = j.getLocation().length() > 18 ? j.getLocation().substring(0, 15) + "..." : j.getLocation();
            System.out.printf(format, j.getId(), title, company, loc, String.format("%.2f", j.getSalary()), j.getJobType().getDisplayName());
        }
        System.out.println(CYAN + "+-------+---------------------------+--------------------+--------------------+--------------+------------+" + RESET);
    }

    public static void printApplicationsTable(List<Application> apps) {
        if (apps == null || apps.isEmpty()) {
            printInfo("No job applications found.");
            return;
        }

        String format = "| %-5s | %-5s | %-25s | %-18s | %-20s | %-12s |%n";
        System.out.println(CYAN + "+-------+-------+---------------------------+--------------------+----------------------+--------------+" + RESET);
        System.out.printf(BOLD + format + RESET, "App ID", "Job ID", "Job Title", "Company", "Status", "Applied On");
        System.out.println(CYAN + "+-------+-------+---------------------------+--------------------+----------------------+--------------+" + RESET);

        for (Application a : apps) {
            String title = a.getJobTitle() != null ? (a.getJobTitle().length() > 25 ? a.getJobTitle().substring(0, 22) + "..." : a.getJobTitle()) : "N/A";
            String company = a.getCompany() != null ? (a.getCompany().length() > 18 ? a.getCompany().substring(0, 15) + "..." : a.getCompany()) : "N/A";
            String dateStr = a.getAppliedAt() != null ? a.getAppliedAt().toString().substring(0, 10) : "N/A";

            String statusColor = getStatusColor(a.getStatus().name());
            String statusFormatted = statusColor + a.getStatus().getDescription() + RESET;

            // Pad status string manually for clean column alignment
            System.out.printf("| %-5d | %-5d | %-25s | %-18s | " + statusColor + "%-20s" + RESET + " | %-12s |%n",
                    a.getId(), a.getJobId(), title, company, a.getStatus().getDescription(), dateStr);
        }
        System.out.println(CYAN + "+-------+-------+---------------------------+--------------------+----------------------+--------------+" + RESET);
    }

    public static void printJobDetail(Job job) {
        System.out.println(CYAN + "==========================================================================================" + RESET);
        System.out.println(BOLD + "Job ID          : " + RESET + job.getId());
        System.out.println(BOLD + "Title           : " + RESET + job.getTitle());
        System.out.println(BOLD + "Company         : " + RESET + job.getCompany());
        System.out.println(BOLD + "Location        : " + RESET + job.getLocation());
        System.out.println(BOLD + "Salary          : " + RESET + String.format("$%.2f / year", job.getSalary()));
        System.out.println(BOLD + "Experience Req. : " + RESET + job.getExperienceYears() + " year(s)");
        System.out.println(BOLD + "Job Type        : " + RESET + job.getJobType().getDisplayName());
        System.out.println(BOLD + "Status          : " + RESET + (job.isActive() ? GREEN + "Active" + RESET : RED + "Closed" + RESET));
        System.out.println(BOLD + "Description     : " + RESET + job.getDescription());
        System.out.println(CYAN + "==========================================================================================" + RESET);
    }

    private static String getStatusColor(String status) {
        switch (status) {
            case "ACCEPTED": return GREEN + BOLD;
            case "SHORTLISTED":
            case "INTERVIEW_SCHEDULED": return CYAN + BOLD;
            case "UNDER_REVIEW": return YELLOW;
            case "REJECTED": return RED;
            case "WITHDRAWN": return BLACK + BOLD;
            default: return WHITE;
        }
    }
}
