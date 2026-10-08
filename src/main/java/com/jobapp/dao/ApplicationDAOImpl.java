package com.jobapp.dao;

import com.jobapp.config.DatabaseConfig;
import com.jobapp.exception.DatabaseException;
import com.jobapp.model.Application;
import com.jobapp.model.ApplicationStatus;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ApplicationDAOImpl implements ApplicationDAO {

    @Override
    public Application applyForJob(Application application) throws DatabaseException {
        String sql = "INSERT INTO applications (job_id, applicant_id, cover_letter, status) VALUES (?, ?, ?, ?)";
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            pstmt.setInt(1, application.getJobId());
            pstmt.setInt(2, application.getApplicantId());
            pstmt.setString(3, application.getCoverLetter());
            pstmt.setString(4, application.getStatus().name());

            int affectedRows = pstmt.executeUpdate();
            if (affectedRows == 0) {
                throw new DatabaseException("Applying for job failed, no rows affected.");
            }

            try (ResultSet generatedKeys = pstmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    application.setId(generatedKeys.getInt(1));
                } else {
                    throw new DatabaseException("Applying for job failed, no ID obtained.");
                }
            }
            return application;
        } catch (SQLException e) {
            throw new DatabaseException("Error applying for job: " + e.getMessage(), e);
        }
    }

    @Override
    public Optional<Application> findById(int id) throws DatabaseException {
        String sql = "SELECT a.*, j.title as job_title, j.company as company_name, u.name as applicant_name, u.email as applicant_email " +
                     "FROM applications a " +
                     "JOIN jobs j ON a.job_id = j.id " +
                     "JOIN users u ON a.applicant_id = u.id " +
                     "WHERE a.id = ?";
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, id);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(mapResultSetToApplication(rs));
                }
            }
        } catch (SQLException e) {
            throw new DatabaseException("Error finding application by ID: " + e.getMessage(), e);
        }
        return Optional.empty();
    }

    @Override
    public Optional<Application> findByJobAndApplicant(int jobId, int applicantId) throws DatabaseException {
        String sql = "SELECT a.*, j.title as job_title, j.company as company_name, u.name as applicant_name, u.email as applicant_email " +
                     "FROM applications a " +
                     "JOIN jobs j ON a.job_id = j.id " +
                     "JOIN users u ON a.applicant_id = u.id " +
                     "WHERE a.job_id = ? AND a.applicant_id = ?";
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, jobId);
            pstmt.setInt(2, applicantId);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(mapResultSetToApplication(rs));
                }
            }
        } catch (SQLException e) {
            throw new DatabaseException("Error finding application by job & applicant: " + e.getMessage(), e);
        }
        return Optional.empty();
    }

    @Override
    public List<Application> findByApplicantId(int applicantId) throws DatabaseException {
        List<Application> apps = new ArrayList<>();
        String sql = "SELECT a.*, j.title as job_title, j.company as company_name, u.name as applicant_name, u.email as applicant_email " +
                     "FROM applications a " +
                     "JOIN jobs j ON a.job_id = j.id " +
                     "JOIN users u ON a.applicant_id = u.id " +
                     "WHERE a.applicant_id = ? ORDER BY a.applied_at DESC";
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, applicantId);
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    apps.add(mapResultSetToApplication(rs));
                }
            }
        } catch (SQLException e) {
            throw new DatabaseException("Error fetching applications for applicant: " + e.getMessage(), e);
        }
        return apps;
    }

    @Override
    public List<Application> findByJobId(int jobId) throws DatabaseException {
        List<Application> apps = new ArrayList<>();
        String sql = "SELECT a.*, j.title as job_title, j.company as company_name, u.name as applicant_name, u.email as applicant_email " +
                     "FROM applications a " +
                     "JOIN jobs j ON a.job_id = j.id " +
                     "JOIN users u ON a.applicant_id = u.id " +
                     "WHERE a.job_id = ? ORDER BY a.applied_at DESC";
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, jobId);
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    apps.add(mapResultSetToApplication(rs));
                }
            }
        } catch (SQLException e) {
            throw new DatabaseException("Error fetching applications for job: " + e.getMessage(), e);
        }
        return apps;
    }

    @Override
    public List<Application> findByRecruiterId(int recruiterId) throws DatabaseException {
        List<Application> apps = new ArrayList<>();
        String sql = "SELECT a.*, j.title as job_title, j.company as company_name, u.name as applicant_name, u.email as applicant_email " +
                     "FROM applications a " +
                     "JOIN jobs j ON a.job_id = j.id " +
                     "JOIN users u ON a.applicant_id = u.id " +
                     "WHERE j.recruiter_id = ? ORDER BY a.applied_at DESC";
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, recruiterId);
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    apps.add(mapResultSetToApplication(rs));
                }
            }
        } catch (SQLException e) {
            throw new DatabaseException("Error fetching applications for recruiter: " + e.getMessage(), e);
        }
        return apps;
    }

    @Override
    public boolean updateStatus(int applicationId, ApplicationStatus status) throws DatabaseException {
        String sql = "UPDATE applications SET status = ? WHERE id = ?";
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, status.name());
            pstmt.setInt(2, applicationId);

            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new DatabaseException("Error updating application status: " + e.getMessage(), e);
        }
    }

    @Override
    public boolean withdrawApplication(int applicationId, int applicantId) throws DatabaseException {
        String sql = "UPDATE applications SET status = 'WITHDRAWN' WHERE id = ? AND applicant_id = ?";
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, applicationId);
            pstmt.setInt(2, applicantId);

            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new DatabaseException("Error withdrawing application: " + e.getMessage(), e);
        }
    }

    @Override
    public int countTotalApplications() throws DatabaseException {
        String sql = "SELECT COUNT(*) FROM applications";
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {

            if (rs.next()) {
                return rs.getInt(1);
            }
        } catch (SQLException e) {
            throw new DatabaseException("Error counting total applications: " + e.getMessage(), e);
        }
        return 0;
    }

    private Application mapResultSetToApplication(ResultSet rs) throws SQLException {
        Application app = new Application(
            rs.getInt("id"),
            rs.getInt("job_id"),
            rs.getInt("applicant_id"),
            rs.getString("cover_letter"),
            ApplicationStatus.fromString(rs.getString("status")),
            rs.getTimestamp("applied_at"),
            rs.getTimestamp("updated_at")
        );
        try {
            app.setJobTitle(rs.getString("job_title"));
            app.setCompany(rs.getString("company_name"));
            app.setApplicantName(rs.getString("applicant_name"));
            app.setApplicantEmail(rs.getString("applicant_email"));
        } catch (SQLException ignored) {}
        return app;
    }
}
