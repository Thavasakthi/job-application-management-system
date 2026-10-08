package com.jobapp.dao;

import com.jobapp.config.DatabaseConfig;
import com.jobapp.exception.DatabaseException;
import com.jobapp.model.Job;
import com.jobapp.model.JobType;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class JobDAOImpl implements JobDAO {

    @Override
    public Job createJob(Job job) throws DatabaseException {
        String sql = "INSERT INTO jobs (title, company, location, salary, experience_years, job_type, description, recruiter_id, is_active) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            pstmt.setString(1, job.getTitle());
            pstmt.setString(2, job.getCompany());
            pstmt.setString(3, job.getLocation());
            pstmt.setDouble(4, job.getSalary());
            pstmt.setInt(5, job.getExperienceYears());
            pstmt.setString(6, job.getJobType().name());
            pstmt.setString(7, job.getDescription());
            pstmt.setInt(8, job.getRecruiterId());
            pstmt.setBoolean(9, job.isActive());

            int affectedRows = pstmt.executeUpdate();
            if (affectedRows == 0) {
                throw new DatabaseException("Creating job failed, no rows affected.");
            }

            try (ResultSet generatedKeys = pstmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    job.setId(generatedKeys.getInt(1));
                } else {
                    throw new DatabaseException("Creating job failed, no ID obtained.");
                }
            }
            return job;
        } catch (SQLException e) {
            throw new DatabaseException("Error creating job: " + e.getMessage(), e);
        }
    }

    @Override
    public Optional<Job> findById(int id) throws DatabaseException {
        String sql = "SELECT j.*, u.name as recruiter_name FROM jobs j LEFT JOIN users u ON j.recruiter_id = u.id WHERE j.id = ?";
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, id);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(mapResultSetToJob(rs));
                }
            }
        } catch (SQLException e) {
            throw new DatabaseException("Error finding job by ID: " + e.getMessage(), e);
        }
        return Optional.empty();
    }

    @Override
    public List<Job> findAll() throws DatabaseException {
        List<Job> jobs = new ArrayList<>();
        String sql = "SELECT j.*, u.name as recruiter_name FROM jobs j LEFT JOIN users u ON j.recruiter_id = u.id ORDER BY j.id DESC";
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {

            while (rs.next()) {
                jobs.add(mapResultSetToJob(rs));
            }
        } catch (SQLException e) {
            throw new DatabaseException("Error fetching all jobs: " + e.getMessage(), e);
        }
        return jobs;
    }

    @Override
    public List<Job> findActiveJobs() throws DatabaseException {
        List<Job> jobs = new ArrayList<>();
        String sql = "SELECT j.*, u.name as recruiter_name FROM jobs j LEFT JOIN users u ON j.recruiter_id = u.id WHERE j.is_active = TRUE ORDER BY j.id DESC";
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {

            while (rs.next()) {
                jobs.add(mapResultSetToJob(rs));
            }
        } catch (SQLException e) {
            throw new DatabaseException("Error fetching active jobs: " + e.getMessage(), e);
        }
        return jobs;
    }

    @Override
    public List<Job> searchByTitleOrCompany(String keyword) throws DatabaseException {
        List<Job> jobs = new ArrayList<>();
        String sql = "SELECT j.*, u.name as recruiter_name FROM jobs j LEFT JOIN users u ON j.recruiter_id = u.id " +
                     "WHERE (LOWER(j.title) LIKE LOWER(?) OR LOWER(j.company) LIKE LOWER(?)) AND j.is_active = TRUE ORDER BY j.id DESC";
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            String term = "%" + (keyword == null ? "" : keyword.trim()) + "%";
            pstmt.setString(1, term);
            pstmt.setString(2, term);

            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    jobs.add(mapResultSetToJob(rs));
                }
            }
        } catch (SQLException e) {
            throw new DatabaseException("Error searching jobs: " + e.getMessage(), e);
        }
        return jobs;
    }

    @Override
    public List<Job> filterJobs(String location, JobType jobType, double minSalary) throws DatabaseException {
        List<Job> jobs = new ArrayList<>();
        StringBuilder sql = new StringBuilder("SELECT j.*, u.name as recruiter_name FROM jobs j LEFT JOIN users u ON j.recruiter_id = u.id WHERE j.is_active = TRUE ");
        
        List<Object> params = new ArrayList<>();
        if (location != null && !location.trim().isEmpty()) {
            sql.append("AND LOWER(j.location) LIKE LOWER(?) ");
            params.add("%" + location.trim() + "%");
        }
        if (jobType != null) {
            sql.append("AND j.job_type = ? ");
            params.add(jobType.name());
        }
        if (minSalary > 0) {
            sql.append("AND j.salary >= ? ");
            params.add(minSalary);
        }
        sql.append("ORDER BY j.id DESC");

        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql.toString())) {

            for (int i = 0; i < params.size(); i++) {
                pstmt.setObject(i + 1, params.get(i));
            }

            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    jobs.add(mapResultSetToJob(rs));
                }
            }
        } catch (SQLException e) {
            throw new DatabaseException("Error filtering jobs: " + e.getMessage(), e);
        }
        return jobs;
    }

    @Override
    public List<Job> findByRecruiter(int recruiterId) throws DatabaseException {
        List<Job> jobs = new ArrayList<>();
        String sql = "SELECT j.*, u.name as recruiter_name FROM jobs j LEFT JOIN users u ON j.recruiter_id = u.id WHERE j.recruiter_id = ? ORDER BY j.id DESC";
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, recruiterId);
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    jobs.add(mapResultSetToJob(rs));
                }
            }
        } catch (SQLException e) {
            throw new DatabaseException("Error fetching jobs by recruiter: " + e.getMessage(), e);
        }
        return jobs;
    }

    @Override
    public boolean updateJob(Job job) throws DatabaseException {
        String sql = "UPDATE jobs SET title = ?, company = ?, location = ?, salary = ?, experience_years = ?, job_type = ?, description = ?, is_active = ? WHERE id = ?";
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, job.getTitle());
            pstmt.setString(2, job.getCompany());
            pstmt.setString(3, job.getLocation());
            pstmt.setDouble(4, job.getSalary());
            pstmt.setInt(5, job.getExperienceYears());
            pstmt.setString(6, job.getJobType().name());
            pstmt.setString(7, job.getDescription());
            pstmt.setBoolean(8, job.isActive());
            pstmt.setInt(9, job.getId());

            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new DatabaseException("Error updating job: " + e.getMessage(), e);
        }
    }

    @Override
    public boolean deleteJob(int id) throws DatabaseException {
        String sql = "DELETE FROM jobs WHERE id = ?";
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, id);
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new DatabaseException("Error deleting job: " + e.getMessage(), e);
        }
    }

    private Job mapResultSetToJob(ResultSet rs) throws SQLException {
        Job job = new Job(
            rs.getInt("id"),
            rs.getString("title"),
            rs.getString("company"),
            rs.getString("location"),
            rs.getDouble("salary"),
            rs.getInt("experience_years"),
            JobType.fromString(rs.getString("job_type")),
            rs.getString("description"),
            rs.getInt("recruiter_id"),
            rs.getBoolean("is_active"),
            rs.getTimestamp("created_at")
        );
        try {
            job.setRecruiterName(rs.getString("recruiter_name"));
        } catch (SQLException ignored) {}
        return job;
    }
}
