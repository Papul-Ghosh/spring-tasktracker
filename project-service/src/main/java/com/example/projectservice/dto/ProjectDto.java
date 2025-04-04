package com.example.projectservice.dto;

import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@NoArgsConstructor
@AllArgsConstructor
public class ProjectDto {
    @NotEmpty(message = "project name cannot be empty")
    private String name;

    @NotEmpty(message = "project description cannot be empty")
    private String description;

    @NotEmpty(message = "project start date cannot be empty")
    private String startDate;

    @NotEmpty(message = "project end date cannot be empty")
    private String endDate;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }
}