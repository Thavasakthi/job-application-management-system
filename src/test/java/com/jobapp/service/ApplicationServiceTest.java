package com.jobapp.service;

import com.jobapp.dao.ApplicationDAOInMemoryImpl;
import com.jobapp.dao.JobDAOInMemoryImpl;
import com.jobapp.exception.ApplicationException;
import com.jobapp.model.Application;
import com.jobapp.model.ApplicationStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class ApplicationServiceTest {

    private ApplicationService applicationService;

    @BeforeEach
    public void setUp() {
        applicationService = new ApplicationService(new ApplicationDAOInMemoryImpl(), new JobDAOInMemoryImpl());
    }

    @Test
    @DisplayName("Test Apply For Job Success")
    public void testApplyForJobSuccess() throws Exception {
        Application app = applicationService.applyForJob(1, 6, "Interested in Senior Java Developer post");
        assertNotNull(app);
        assertTrue(app.getId() > 0);
        assertEquals(ApplicationStatus.APPLIED, app.getStatus());
    }

    @Test
    @DisplayName("Test Duplicate Job Application Throws ApplicationException")
    public void testDuplicateApplicationThrowsException() throws Exception {
        // Job 1 already applied by Applicant 4 in seed data
        assertThrows(ApplicationException.class, () -> {
            applicationService.applyForJob(1, 4, "Trying to apply twice");
        });
    }

    @Test
    @DisplayName("Test Update Application Status")
    public void testUpdateStatusSuccess() throws Exception {
        boolean updated = applicationService.updateApplicationStatus(1, ApplicationStatus.ACCEPTED);
        assertTrue(updated);

        Application app = applicationService.getApplicationById(1);
        assertEquals(ApplicationStatus.ACCEPTED, app.getStatus());
    }

    @Test
    @DisplayName("Test View Applicant Applications")
    public void testGetApplicantApplications() throws Exception {
        List<Application> apps = applicationService.getApplicantApplications(6);
        assertNotNull(apps);
        assertFalse(apps.isEmpty());
    }
}
