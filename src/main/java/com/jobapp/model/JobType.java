package com.jobapp.model;

public enum JobType {
    FULL_TIME("Full-Time"),
    PART_TIME("Part-Time"),
    REMOTE("Remote"),
    INTERNSHIP("Internship"),
    CONTRACT("Contract");

    private final String displayName;

    JobType(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }

    public static JobType fromString(String str) {
        if (str == null) return FULL_TIME;
        for (JobType type : values()) {
            if (type.name().equalsIgnoreCase(str) || type.displayName.equalsIgnoreCase(str)) {
                return type;
            }
        }
        return FULL_TIME;
    }
}
