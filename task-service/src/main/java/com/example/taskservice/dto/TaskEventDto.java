package com.example.taskservice.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TaskEventDto {

    private String eventType;
    private TaskProjectDto taskProjectDto;

}