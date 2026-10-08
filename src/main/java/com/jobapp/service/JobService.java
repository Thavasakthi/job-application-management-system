package com.jobapp.service;

import com.jobapp.dao.DAOFactory;
import com.jobapp.dao.JobDAO;
import com.jobapp.exception.DatabaseException;
import com.jobapp.exception.InvalidInputException;
import com.jobapp.exception.JobNotFoundException;
import com.jobapp.model.Job;
import com.jobapp.model.JobType;

import java.util.List;

public class JobService {
    private final JobDAO jobDAO;

    public JobService() {
        this.jobDAO = DAOFactory.getJobDAO();
    }

    public JobService(JobDAO jobDAO) {
        this.jobDAO = jobDAO;
    }

    public Job addJob(String title, String company, String location, double salary,
                      int experienceYears, JobType jobType, String description, int recruiterId)
            throws InvalidInputException, DatabaseException {
        if (title == null || title.trim().isEmpty()) {
            throw new InvalidInputException("Job title cannot be empty.");
        }
        if (company == null || company.trim().isEmpty()) {
            throw new InvalidInputException("Company name cannot be empty.");
        }
        if (location == null || location.trim().isEmpty()) {
            throw new InvalidInputException("Location cannot be empty.");
        }
        if (salary <= 0) {
            throw new InvalidInputException("Salary must be greater than 0.");
        }
        if (experienceYears < 0) {
            throw new InvalidInputException("Experience years cannot be negative.");
        }

        Job job = new Job(title.trim(), company.trim(), location.trim(), salary, experienceYears,
                jobType != null ? jobType : JobType.FULL_TIME, description != null ? description.trim() : "", recruiterId);

        return jobDAO.createJob(job);
    }

    public Job getJobById(int jobId) throws JobNotFoundException, DatabaseException {
        return jobDAO.findById(jobId)
                .orElseThrow(() -> new JobNotFoundException("Job posting not found with ID: " + jobId));
    }

    public List<Job> getAllActiveJobs() throws DatabaseException {
        return jobDAO.findActiveJobs();
    }

    public List<Job> searchJobs(String keyword) throws DatabaseException {
        if (keyword == null || keyword.trim().isEmpty()) {
            return getAllActiveJobs();
        }
        return jobDAO.searchByTitleOrCompany(keyword.trim());
    }

    public List<Job> filterJobs(String location, JobType jobType, double minSalary) throws DatabaseException {
        return jobDAO.filterJobs(location, jobType, minSalary);
    }

    public List<Job> getJobsByRecruiter(int recruiterId) throws DatabaseException {
        return jobDAO.findByRecruiter(recruiterId);
    }

    public boolean updateJobStatus(int jobId, boolean active) throws JobNotFoundException, DatabaseException {
        Job job = getJobById(jobId);
        job.setActive(active);
        return jobDAO.updateJob(job);
    }

    public boolean deleteJob(int jobId) throws JobNotFoundException, DatabaseException {
        // verify existence
        getJobById(jobId);
        return jobDAO.deleteJob(jobId);
    }
}
