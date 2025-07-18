package com.example.taskservice.dto;

import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TaskProjectDto {
    @NotEmpty(message = "project id cannot be empty")
    private Long projectId;

    @NotEmpty(message = "task id cannot be empty")
    private String taskId;
    private String TaskTitle;
    private Long taskOwnerId;

    private LocalDateTime updatedAt;

}