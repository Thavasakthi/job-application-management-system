package com.jobapp.model;

import java.sql.Timestamp;

public class Application {
    private int id;
    private int jobId;
    private String jobTitle;
    private String company;
    private int applicantId;
    private String applicantName;
    private String applicantEmail;
    private String coverLetter;
    private ApplicationStatus status;
    private Timestamp appliedAt;
    private Timestamp updatedAt;

    public Application() {}

    public Application(int id, int jobId, int applicantId, String coverLetter, ApplicationStatus status, Timestamp appliedAt, Timestamp updatedAt) {
        this.id = id;
        this.jobId = jobId;
        this.applicantId = applicantId;
        this.coverLetter = coverLetter;
        this.status = status;
        this.appliedAt = appliedAt;
        this.updatedAt = updatedAt;
    }

    public Application(int jobId, int applicantId, String coverLetter) {
        this(0, jobId, applicantId, coverLetter, ApplicationStatus.APPLIED, new Timestamp(System.currentTimeMillis()), new Timestamp(System.currentTimeMillis()));
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getJobId() {
        return jobId;
    }

    public void setJobId(int jobId) {
        this.jobId = jobId;
    }

    public String getJobTitle() {
        return jobTitle;
    }

    public void setJobTitle(String jobTitle) {
        this.jobTitle = jobTitle;
    }

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public int getApplicantId() {
        return applicantId;
    }

    public void setApplicantId(int applicantId) {
        this.applicantId = applicantId;
    }

    public String getApplicantName() {
        return applicantName;
    }

    public void setApplicantName(String applicantName) {
        this.applicantName = applicantName;
    }

    public String getApplicantEmail() {
        return applicantEmail;
    }

    public void setApplicantEmail(String applicantEmail) {
        this.applicantEmail = applicantEmail;
    }

    public String getCoverLetter() {
        return coverLetter;
    }

    public void setCoverLetter(String coverLetter) {
        this.coverLetter = coverLetter;
    }

    public ApplicationStatus getStatus() {
        return status;
    }

    public void setStatus(ApplicationStatus status) {
        this.status = status;
    }

    public Timestamp getAppliedAt() {
        return appliedAt;
    }

    public void setAppliedAt(Timestamp appliedAt) {
        this.appliedAt = appliedAt;
    }

    public Timestamp getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Timestamp updatedAt) {
        this.updatedAt = updatedAt;
    }

    @Override
    public String toString() {
        return String.format("Application[ID=%d, JobID=%d, ApplicantID=%d, Status=%s]", id, jobId, applicantId, status);
    }
}
