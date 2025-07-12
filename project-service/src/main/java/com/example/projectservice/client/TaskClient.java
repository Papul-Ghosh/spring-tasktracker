package com.example.projectservice.client;

import com.example.projectservice.dto.TaskDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@FeignClient(name = "task-service", url = "${taskservice.url}")
public interface TaskClient {
    @GetMapping("${taskservice.tasksbyproject.url}/{projectId}")
    List<TaskDto> getTasksByProjectId(@PathVariable ("projectId") Long projectId);

    @GetMapping("/{taskId}")
    TaskDto getTaskById(@PathVariable ("taskId") String taskId);
}