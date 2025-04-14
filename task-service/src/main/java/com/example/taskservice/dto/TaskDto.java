package com.example.taskservice.dto;

import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TaskDto {
    @NotEmpty(message = "task title cannot be empty")
    private String title;

    @NotEmpty(message = "task description cannot be empty")
    private String description;

    @NotEmpty(message = "task id cannot be empty")
    private Long projectId;

    @NotEmpty(message = "task priority cannot be empty")
    private String priority;


    @NotEmpty(message = "task story point cannot be empty")
    private Long storyPoint;

}