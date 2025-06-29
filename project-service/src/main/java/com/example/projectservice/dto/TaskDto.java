package com.example.projectservice.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TaskDto {
    private String id;
    private String title;
    private Long assigneeId;
    private Long ownerId;
    private String status;
    private String priority;
    private Long storyPoint;
}