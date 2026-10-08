package com.jobapp.dao;

import com.jobapp.exception.DatabaseException;
import com.jobapp.model.Application;
import com.jobapp.model.ApplicationStatus;

import java.util.List;
import java.util.Optional;

public interface ApplicationDAO {
    Application applyForJob(Application application) throws DatabaseException;
    Optional<Application> findById(int id) throws DatabaseException;
    Optional<Application> findByJobAndApplicant(int jobId, int applicantId) throws DatabaseException;
    List<Application> findByApplicantId(int applicantId) throws DatabaseException;
    List<Application> findByJobId(int jobId) throws DatabaseException;
    List<Application> findByRecruiterId(int recruiterId) throws DatabaseException;
    boolean updateStatus(int applicationId, ApplicationStatus status) throws DatabaseException;
    boolean withdrawApplication(int applicationId, int applicantId) throws DatabaseException;
    int countTotalApplications() throws DatabaseException;
}
