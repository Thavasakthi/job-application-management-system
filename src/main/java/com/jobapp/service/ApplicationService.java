package com.jobapp.service;

import com.jobapp.dao.ApplicationDAO;
import com.jobapp.dao.DAOFactory;
import com.jobapp.dao.JobDAO;
import com.jobapp.exception.ApplicationException;
import com.jobapp.exception.DatabaseException;
import com.jobapp.exception.InvalidInputException;
import com.jobapp.exception.JobNotFoundException;
import com.jobapp.model.Application;
import com.jobapp.model.ApplicationStatus;
import com.jobapp.model.Job;

import java.util.List;
import java.util.Optional;

public class ApplicationService {
    private final ApplicationDAO applicationDAO;
    private final JobDAO jobDAO;

    public ApplicationService() {
        this.applicationDAO = DAOFactory.getApplicationDAO();
        this.jobDAO = DAOFactory.getJobDAO();
    }

    public ApplicationService(ApplicationDAO applicationDAO, JobDAO jobDAO) {
        this.applicationDAO = applicationDAO;
        this.jobDAO = jobDAO;
    }

    public Application applyForJob(int jobId, int applicantId, String coverLetter)
            throws JobNotFoundException, ApplicationException, InvalidInputException, DatabaseException {
        
        Optional<Job> jobOpt = jobDAO.findById(jobId);
        if (jobOpt.isEmpty()) {
            throw new JobNotFoundException("Cannot apply: Job ID " + jobId + " does not exist.");
        }

        Job job = jobOpt.get();
        if (!job.isActive()) {
            throw new ApplicationException("Cannot apply: Job posting is no longer active.");
        }

        Optional<Application> existing = applicationDAO.findByJobAndApplicant(jobId, applicantId);
        if (existing.isPresent()) {
            throw new ApplicationException("You have already submitted an application for '" + job.getTitle() + "' at " + job.getCompany() + ".");
        }

        Application app = new Application(jobId, applicantId, coverLetter != null ? coverLetter.trim() : "");
        app.setJobTitle(job.getTitle());
        app.setCompany(job.getCompany());

        return applicationDAO.applyForJob(app);
    }

    public List<Application> getApplicantApplications(int applicantId) throws DatabaseException {
        return applicationDAO.findByApplicantId(applicantId);
    }

    public List<Application> getApplicationsForJob(int jobId) throws DatabaseException {
        return applicationDAO.findByJobId(jobId);
    }

    public List<Application> getApplicationsForRecruiter(int recruiterId) throws DatabaseException {
        return applicationDAO.findByRecruiterId(recruiterId);
    }

    public boolean updateApplicationStatus(int applicationId, ApplicationStatus status)
            throws ApplicationException, InvalidInputException, DatabaseException {
        if (status == null) {
            throw new InvalidInputException("Application status cannot be null.");
        }

        Optional<Application> appOpt = applicationDAO.findById(applicationId);
        if (appOpt.isEmpty()) {
            throw new ApplicationException("Application not found with ID: " + applicationId);
        }

        return applicationDAO.updateStatus(applicationId, status);
    }

    public boolean withdrawApplication(int applicationId, int applicantId)
            throws ApplicationException, DatabaseException {
        Optional<Application> appOpt = applicationDAO.findById(applicationId);
        if (appOpt.isEmpty()) {
            throw new ApplicationException("Application not found with ID: " + applicationId);
        }

        Application app = appOpt.get();
        if (app.getApplicantId() != applicantId) {
            throw new ApplicationException("You are not authorized to withdraw this application.");
        }

        if (app.getStatus() == ApplicationStatus.ACCEPTED || app.getStatus() == ApplicationStatus.REJECTED) {
            throw new ApplicationException("Cannot withdraw application after final decision (" + app.getStatus() + ").");
        }

        return applicationDAO.withdrawApplication(applicationId, applicantId);
    }

    public Application getApplicationById(int applicationId) throws ApplicationException, DatabaseException {
        return applicationDAO.findById(applicationId)
                .orElseThrow(() -> new ApplicationException("Application not found with ID: " + applicationId));
    }
}
