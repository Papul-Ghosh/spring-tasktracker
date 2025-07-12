package com.example.projectservice.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ProjectEventDto {

    private String eventType;
    private ProjectNotificationDto projectNotificationDto;
}