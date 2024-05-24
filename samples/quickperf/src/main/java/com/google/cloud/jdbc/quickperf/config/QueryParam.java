package com.google.cloud.jdbc.quickperf.config;


public class QueryParam {
    private int order;
    private String value;

    // Getters and setters

    public int getOrder() {
        return order;
    }

    public void setOrder(int order) {
        this.order = order;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
