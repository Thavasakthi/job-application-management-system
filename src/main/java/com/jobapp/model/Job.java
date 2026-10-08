package com.jobapp.model;

import java.sql.Timestamp;

public class Job {
    private int id;
    private String title;
    private String company;
    private String location;
    private double salary;
    private int experienceYears;
    private JobType jobType;
    private String description;
    private int recruiterId;
    private String recruiterName;
    private boolean active;
    private Timestamp createdAt;

    public Job() {}

    public Job(int id, String title, String company, String location, double salary,
               int experienceYears, JobType jobType, String description,
               int recruiterId, boolean active, Timestamp createdAt) {
        this.id = id;
        this.title = title;
        this.company = company;
        this.location = location;
        this.salary = salary;
        this.experienceYears = experienceYears;
        this.jobType = jobType;
        this.description = description;
        this.recruiterId = recruiterId;
        this.active = active;
        this.createdAt = createdAt;
    }

    public Job(String title, String company, String location, double salary,
               int experienceYears, JobType jobType, String description, int recruiterId) {
        this(0, title, company, location, salary, experienceYears, jobType, description, recruiterId, true, new Timestamp(System.currentTimeMillis()));
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public double getSalary() {
        return salary;
    }

    public void setSalary(double salary) {
        this.salary = salary;
    }

    public int getExperienceYears() {
        return experienceYears;
    }

    public void setExperienceYears(int experienceYears) {
        this.experienceYears = experienceYears;
    }

    public JobType getJobType() {
        return jobType;
    }

    public void setJobType(JobType jobType) {
        this.jobType = jobType;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getRecruiterId() {
        return recruiterId;
    }

    public void setRecruiterId(int recruiterId) {
        this.recruiterId = recruiterId;
    }

    public String getRecruiterName() {
        return recruiterName;
    }

    public void setRecruiterName(String recruiterName) {
        this.recruiterName = recruiterName;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public Timestamp getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Timestamp createdAt) {
        this.createdAt = createdAt;
    }

    @Override
    public String toString() {
        return String.format("Job[ID=%d, Title='%s', Company='%s', Location='%s', Salary=$%.2f, Type=%s]",
                id, title, company, location, salary, jobType);
    }
}
