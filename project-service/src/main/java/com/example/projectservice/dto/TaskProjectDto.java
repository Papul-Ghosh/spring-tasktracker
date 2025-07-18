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
public class TaskProjectDto {
    @NotEmpty(message = "project id cannot be empty")
    private Long projectId;

    @NotEmpty(message = "task id cannot be empty")
    private String taskId;
    private String taskTitle;
    private Long taskOwnerId;

}