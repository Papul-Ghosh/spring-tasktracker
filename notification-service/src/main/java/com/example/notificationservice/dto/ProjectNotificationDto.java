package com.example.notificationservice.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ProjectNotificationDto {

    private String fullName;
    private String email;

    private Long projectId;
    private String projectName;

    private String TaskId;
    private String TaskName;
    private String TaskOwnerFullName;
}