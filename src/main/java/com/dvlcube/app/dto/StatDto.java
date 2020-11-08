package com.dvlcube.app.dto;

public class StatDto {

    private String action;
    private Long min;
    private Long max;
    private Long total;
    private Long count;
    private Long errors;
    private String avg;

    public StatDto(String action, Long min, Long max, Long total, Long count, Long errors, String avg) {
        this.action = action;
        this.min = min;
        this.max = max;
        this.total = total;
        this.count = count;
        this.errors = errors;
        this.avg = avg;
    }

    public String getAction() {
        return action;
    }

    public Long getMin() {
        return min;
    }

    public Long getMax() {
        return max;
    }

    public Long getTotal() {
        return total;
    }

    public Long getCount() {
        return count;
    }

    public Long getErrors() {
        return errors;
    }

    public String getAvg() {
        return avg;
    }
}
