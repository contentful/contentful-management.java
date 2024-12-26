package com.contentful.java.cma.model;

public enum CMAScheduledActionStatus {
    CANCELED("canceled"),
    FAILED("failed"),
    SCHEDULED("scheduled"),
    SUCCEEDED("succeeded");

    private final String status;

    CMAScheduledActionStatus(String status) {
        this.status = status;
    }

    public String getStatus() {
        return status;
    }

    public static CMAScheduledActionStatus from(String status) {
        for (CMAScheduledActionStatus scheduledActionStatus : values()) {
            if (scheduledActionStatus.status.equals(status)) {
                return scheduledActionStatus;
            }
        }
        return null; // or throw an IllegalArgumentException
    }
}