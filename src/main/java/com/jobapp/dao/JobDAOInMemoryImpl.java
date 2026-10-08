package com.jobapp.dao;

import com.jobapp.exception.DatabaseException;
import com.jobapp.model.Job;
import com.jobapp.model.JobType;

import java.sql.Timestamp;
import java.util.*;
import java.util.stream.Collectors;

public class JobDAOInMemoryImpl implements JobDAO {
    private static final Map<Integer, Job> jobMap = new HashMap<>();
    private static int idCounter = 1;

    public JobDAOInMemoryImpl() {
        if (jobMap.isEmpty()) {
            seedData();
        }
    }

    private void seedData() {
        createJobInternal(new Job("Senior Java Developer", "Google", "Mountain View, CA", 140000.0, 5, JobType.FULL_TIME, "Design and implement scalable backend microservices.", 2));
        createJobInternal(new Job("Full Stack Software Engineer", "Microsoft", "Redmond, WA", 125000.0, 3, JobType.REMOTE, "Develop rich web applications using Java, React, and Azure.", 3));
        createJobInternal(new Job("Java Backend Intern", "Google", "San Jose, CA", 60000.0, 0, JobType.INTERNSHIP, "Join our core engineering team to build scalable APIs.", 2));
        createJobInternal(new Job("Cloud Solutions Architect", "Amazon Web Services", "Seattle, WA", 155000.0, 7, JobType.FULL_TIME, "Architect distributed systems and high-throughput databases.", 3));
        createJobInternal(new Job("Associate Software Engineer", "Zoho Corporation", "Chennai, India", 55000.0, 1, JobType.FULL_TIME, "Develop core SaaS tools using Java, JDBC, and MySQL.", 2));
        createJobInternal(new Job("DevOps Engineer", "TCS", "Bengaluru, India", 85000.0, 4, JobType.REMOTE, "Manage CI/CD pipelines, Docker, Kubernetes, and deployment scripts.", 3));
    }

    private synchronized Job createJobInternal(Job job) {
        job.setId(idCounter++);
        job.setCreatedAt(new Timestamp(System.currentTimeMillis()));
        job.setActive(true);
        jobMap.put(job.getId(), job);
        return job;
    }

    @Override
    public synchronized Job createJob(Job job) throws DatabaseException {
        return createJobInternal(job);
    }

    @Override
    public Optional<Job> findById(int id) throws DatabaseException {
        return Optional.ofNullable(jobMap.get(id));
    }

    @Override
    public List<Job> findAll() throws DatabaseException {
        return jobMap.values().stream()
                .sorted(Comparator.comparingInt(Job::getId).reversed())
                .collect(Collectors.toList());
    }

    @Override
    public List<Job> findActiveJobs() throws DatabaseException {
        return jobMap.values().stream()
                .filter(Job::isActive)
                .sorted(Comparator.comparingInt(Job::getId).reversed())
                .collect(Collectors.toList());
    }

    @Override
    public List<Job> searchByTitleOrCompany(String keyword) throws DatabaseException {
        if (keyword == null || keyword.trim().isEmpty()) {
            return findActiveJobs();
        }
        String lowerKey = keyword.trim().toLowerCase();
        return jobMap.values().stream()
                .filter(Job::isActive)
                .filter(j -> j.getTitle().toLowerCase().contains(lowerKey) || j.getCompany().toLowerCase().contains(lowerKey))
                .sorted(Comparator.comparingInt(Job::getId).reversed())
                .collect(Collectors.toList());
    }

    @Override
    public List<Job> filterJobs(String location, JobType jobType, double minSalary) throws DatabaseException {
        return jobMap.values().stream()
                .filter(Job::isActive)
                .filter(j -> location == null || location.trim().isEmpty() || j.getLocation().toLowerCase().contains(location.trim().toLowerCase()))
                .filter(j -> jobType == null || j.getJobType() == jobType)
                .filter(j -> minSalary <= 0 || j.getSalary() >= minSalary)
                .sorted(Comparator.comparingInt(Job::getId).reversed())
                .collect(Collectors.toList());
    }

    @Override
    public List<Job> findByRecruiter(int recruiterId) throws DatabaseException {
        return jobMap.values().stream()
                .filter(j -> j.getRecruiterId() == recruiterId)
                .sorted(Comparator.comparingInt(Job::getId).reversed())
                .collect(Collectors.toList());
    }

    @Override
    public synchronized boolean updateJob(Job job) throws DatabaseException {
        if (jobMap.containsKey(job.getId())) {
            jobMap.put(job.getId(), job);
            return true;
        }
        return false;
    }

    @Override
    public synchronized boolean deleteJob(int id) throws DatabaseException {
        return jobMap.remove(id) != null;
    }
}
