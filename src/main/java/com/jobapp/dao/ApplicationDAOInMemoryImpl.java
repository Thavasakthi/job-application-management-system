package com.jobapp.dao;

import com.jobapp.exception.DatabaseException;
import com.jobapp.model.Application;
import com.jobapp.model.ApplicationStatus;

import java.sql.Timestamp;
import java.util.*;
import java.util.stream.Collectors;

public class ApplicationDAOInMemoryImpl implements ApplicationDAO {
    private static final Map<Integer, Application> appMap = new HashMap<>();
    private static int idCounter = 1;

    public ApplicationDAOInMemoryImpl() {
        if (appMap.isEmpty()) {
            seedData();
        }
    }

    private void seedData() {
        Application a1 = new Application(1, 4, "I have 5 years of backend engineering experience with Spring Boot and Java 17.");
        a1.setStatus(ApplicationStatus.SHORTLISTED);
        applyForJobInternal(a1);

        Application a2 = new Application(2, 5, "Passionate Full Stack engineer with expert knowledge of Java, REST APIs, and React.");
        a2.setStatus(ApplicationStatus.UNDER_REVIEW);
        applyForJobInternal(a2);

        Application a3 = new Application(3, 6, "Enthusiastic fresh graduate eager to learn core Java internal structures and JDBC.");
        a3.setStatus(ApplicationStatus.APPLIED);
        applyForJobInternal(a3);

        Application a4 = new Application(5, 6, "Strong foundation in Java Data Structures, MySQL queries, and Object Oriented System Design.");
        a4.setStatus(ApplicationStatus.INTERVIEW_SCHEDULED);
        applyForJobInternal(a4);
    }

    private synchronized Application applyForJobInternal(Application application) {
        application.setId(idCounter++);
        Timestamp now = new Timestamp(System.currentTimeMillis());
        application.setAppliedAt(now);
        application.setUpdatedAt(now);
        appMap.put(application.getId(), application);
        return application;
    }

    @Override
    public synchronized Application applyForJob(Application application) throws DatabaseException {
        return applyForJobInternal(application);
    }

    @Override
    public Optional<Application> findById(int id) throws DatabaseException {
        return Optional.ofNullable(appMap.get(id));
    }

    @Override
    public Optional<Application> findByJobAndApplicant(int jobId, int applicantId) throws DatabaseException {
        return appMap.values().stream()
                .filter(a -> a.getJobId() == jobId && a.getApplicantId() == applicantId)
                .findFirst();
    }

    @Override
    public List<Application> findByApplicantId(int applicantId) throws DatabaseException {
        return appMap.values().stream()
                .filter(a -> a.getApplicantId() == applicantId)
                .sorted(Comparator.comparing(Application::getAppliedAt).reversed())
                .collect(Collectors.toList());
    }

    @Override
    public List<Application> findByJobId(int jobId) throws DatabaseException {
        return appMap.values().stream()
                .filter(a -> a.getJobId() == jobId)
                .sorted(Comparator.comparing(Application::getAppliedAt).reversed())
                .collect(Collectors.toList());
    }

    @Override
    public List<Application> findByRecruiterId(int recruiterId) throws DatabaseException {
        // In memory recruiter filter by checking job recruiter id
        JobDAO jobDAO = new JobDAOInMemoryImpl();
        try {
            Set<Integer> recruiterJobIds = jobDAO.findByRecruiter(recruiterId).stream()
                    .map(j -> j.getId())
                    .collect(Collectors.toSet());

            return appMap.values().stream()
                    .filter(a -> recruiterJobIds.contains(a.getJobId()))
                    .sorted(Comparator.comparing(Application::getAppliedAt).reversed())
                    .collect(Collectors.toList());
        } catch (DatabaseException e) {
            return Collections.emptyList();
        }
    }

    @Override
    public synchronized boolean updateStatus(int applicationId, ApplicationStatus status) throws DatabaseException {
        Application app = appMap.get(applicationId);
        if (app != null) {
            app.setStatus(status);
            app.setUpdatedAt(new Timestamp(System.currentTimeMillis()));
            return true;
        }
        return false;
    }

    @Override
    public synchronized boolean withdrawApplication(int applicationId, int applicantId) throws DatabaseException {
        Application app = appMap.get(applicationId);
        if (app != null && app.getApplicantId() == applicantId) {
            app.setStatus(ApplicationStatus.WITHDRAWN);
            app.setUpdatedAt(new Timestamp(System.currentTimeMillis()));
            return true;
        }
        return false;
    }

    @Override
    public int countTotalApplications() throws DatabaseException {
        return appMap.size();
    }
}
