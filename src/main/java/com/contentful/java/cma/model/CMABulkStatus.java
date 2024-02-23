package com.contentful.java.cma.model;

public enum CMABulkStatus {
    CREATED("created"),
    IN_PROGRESS("inProgress"),
    SUCCEEDED("succeeded"),
    FAILED("failed");

    private final String status;

    CMABulkStatus(String status) {
        this.status = status;
    }

    public String getStatus() {
        return status;
    }

    public static CMABulkStatus from(String status) {
        for (CMABulkStatus bulkStatus : values()) {
            if (bulkStatus.status.equals(status)) {
                return bulkStatus;
            }
        }
        return null; // or throw an IllegalArgumentException
    }
}