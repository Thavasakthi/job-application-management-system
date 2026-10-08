package com.jobapp.service;

import com.jobapp.dao.JobDAOInMemoryImpl;
import com.jobapp.exception.InvalidInputException;
import com.jobapp.exception.JobNotFoundException;
import com.jobapp.model.Job;
import com.jobapp.model.JobType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class JobServiceTest {

    private JobService jobService;

    @BeforeEach
    public void setUp() {
        jobService = new JobService(new JobDAOInMemoryImpl());
    }

    @Test
    @DisplayName("Test Add Job with Valid Inputs")
    public void testAddJobSuccess() throws Exception {
        Job job = jobService.addJob("Software Engineer", "Google", "Mountain View, CA", 130000.0, 3, JobType.FULL_TIME, "Java Development", 1);
        assertNotNull(job);
        assertTrue(job.getId() > 0);
        assertEquals("Software Engineer", job.getTitle());
        assertEquals("Google", job.getCompany());
    }

    @Test
    @DisplayName("Test Add Job with Invalid Salary throws Exception")
    public void testAddJobInvalidSalary() {
        assertThrows(InvalidInputException.class, () -> {
            jobService.addJob("DevOps", "AWS", "Seattle", -500.0, 2, JobType.REMOTE, "CI/CD", 1);
        });
    }

    @Test
    @DisplayName("Test Search Jobs by Keyword")
    public void testSearchJobs() throws Exception {
        List<Job> results = jobService.searchJobs("Google");
        assertNotNull(results);
        assertFalse(results.isEmpty());
        assertTrue(results.stream().allMatch(j -> j.getCompany().toLowerCase().contains("google") || j.getTitle().toLowerCase().contains("google")));
    }

    @Test
    @DisplayName("Test Get Job by Non-existent ID throws Exception")
    public void testGetJobNotFound() {
        assertThrows(JobNotFoundException.class, () -> {
            jobService.getJobById(99999);
        });
    }
}
