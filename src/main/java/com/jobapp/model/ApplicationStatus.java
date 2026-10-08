package com.jobapp.model;

public enum ApplicationStatus {
    APPLIED("Applied"),
    UNDER_REVIEW("Under Review"),
    SHORTLISTED("Shortlisted"),
    INTERVIEW_SCHEDULED("Interview Scheduled"),
    ACCEPTED("Accepted"),
    REJECTED("Rejected"),
    WITHDRAWN("Withdrawn");

    private final String description;

    ApplicationStatus(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

    public static ApplicationStatus fromString(String str) {
        if (str == null) return APPLIED;
        for (ApplicationStatus status : values()) {
            if (status.name().equalsIgnoreCase(str) || status.description.equalsIgnoreCase(str)) {
                return status;
            }
        }
        return APPLIED;
    }
}
