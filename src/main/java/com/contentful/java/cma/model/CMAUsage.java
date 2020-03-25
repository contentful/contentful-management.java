package com.contentful.java.cma.model;

import java.util.LinkedHashMap;

public class CMAUsage extends CMAResource {
    private String unitOfMeasure;
    private UsageMetric metric;
    private int usage;

    public String getUnitOfMeasure() {
        return unitOfMeasure;
    }

    public void setUnitOfMeasure(String unitOfMeasure) {
        this.unitOfMeasure = unitOfMeasure;
    }

    public UsageMetric getMetric() {
        return metric;
    }

    public void setMetric(UsageMetric metric) {
        this.metric = metric;
    }

    public int getUsage() {
        return usage;
    }

    public void setUsage(int usage) {
        this.usage = usage;
    }

    public LinkedHashMap<String, Integer> getUsagePerDay() {
        return usagePerDay;
    }

    public void setUsagePerDay(LinkedHashMap<String, Integer> usagePerDay) {
        this.usagePerDay = usagePerDay;
    }

    public DateRange getDateRange() {
        return dateRange;
    }

    public void setDateRange(DateRange dateRange) {
        this.dateRange = dateRange;
    }

    private LinkedHashMap<String, Integer> usagePerDay;
    private DateRange dateRange;

    public CMAUsage(CMAType type) {
        super(CMAType.OrganizationPeriodicUsage);
    }

    public static class DateRange {

        private String startAt;
        private String endAt;

        public String getStartAt() {
            return startAt;
        }

        public void setStartAt(String startAt) {
            this.startAt = startAt;
        }

        public String getEndAt() {
            return endAt;
        }

        public void setEndAt(String endAt) {
            this.endAt = endAt;
        }
    }

    public enum UsageMetric {
        cda,
        cma,
        cpa,
        gql1
    }
}
