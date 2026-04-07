package com.employee.management.dto;

import java.util.List;
import java.util.Map;

public class ExportRequest {
    private String format;
    private List<String> fields;
    private Map<String, Object> filters;

    public String getFormat() {
        return format;
    }

    public void setFormat(String format) {
        this.format = format;
    }

    public List<String> getFields() {
        return fields;
    }

    public void setFields(List<String> fields) {
        this.fields = fields;
    }

    public Map<String, Object> getFilters() {
        return filters;
    }

    public void setFilters(Map<String, Object> filters) {
        this.filters = filters;
    }
}
