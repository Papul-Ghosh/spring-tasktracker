package com.example.projectservice.dto;

import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
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
}