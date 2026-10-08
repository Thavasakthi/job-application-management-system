package com.jobapp.model;

public enum Role {
    APPLICANT,
    RECRUITER,
    ADMIN;

    public static Role fromString(String roleStr) {
        if (roleStr == null) return APPLICANT;
        try {
            return Role.valueOf(roleStr.trim().toUpperCase());
        } catch (IllegalArgumentException e) {
            return APPLICANT;
        }
    }
}
