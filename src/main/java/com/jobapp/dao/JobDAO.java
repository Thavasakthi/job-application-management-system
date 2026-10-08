package com.jobapp.dao;

import com.jobapp.exception.DatabaseException;
import com.jobapp.model.Job;
import com.jobapp.model.JobType;

import java.util.List;
import java.util.Optional;

public interface JobDAO {
    Job createJob(Job job) throws DatabaseException;
    Optional<Job> findById(int id) throws DatabaseException;
    List<Job> findAll() throws DatabaseException;
    List<Job> findActiveJobs() throws DatabaseException;
    List<Job> searchByTitleOrCompany(String keyword) throws DatabaseException;
    List<Job> filterJobs(String location, JobType jobType, double minSalary) throws DatabaseException;
    List<Job> findByRecruiter(int recruiterId) throws DatabaseException;
    boolean updateJob(Job job) throws DatabaseException;
    boolean deleteJob(int id) throws DatabaseException;
}
