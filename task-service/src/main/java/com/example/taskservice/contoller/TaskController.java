package com.example.taskservice.contoller;

import com.example.taskservice.client.ProjectClient;
import com.example.taskservice.client.UserClient;
import com.example.taskservice.dto.TaskDto;
import com.example.taskservice.exception.ProjectNotFoundException;
import com.example.taskservice.exception.UserNotFoundException;
import com.example.taskservice.model.Task;
import com.example.taskservice.service.TaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import java.util.List;

@RestController
@RequestMapping("/v1/tasks")
public class TaskController {

    @Autowired
    private final TaskService taskService;
    private final UserClient userClient;
    private final ProjectClient projectClient;

    public TaskController(TaskService taskService, UserClient userClient, ProjectClient projectClient) {
        this.taskService = taskService;
        this.userClient = userClient;
        this.projectClient = projectClient;
    }

    @GetMapping("/{id}")
    public Task getTask(@PathVariable String id) {
        return taskService.getTaskById(id);
    }

    @GetMapping("/project/{projectId}")
    public List<Task> getTasksByProject(@PathVariable Long projectId) {
        if (!projectClient.existsProjectId(projectId)) {
            throw new ProjectNotFoundException("Project not found with id: " + projectId);
        }
        return taskService.getTaskByProjectId(projectId);
    }

    @PostMapping("/create")
    public ResponseEntity<?> createTask(@RequestBody TaskDto taskDto) {
        try {
            Long activeUserId = getActiveUserId();
            if (!projectClient.existsProjectId(taskDto.getProjectId())) {
                throw new ProjectNotFoundException("Project not found with id: " + taskDto.getProjectId());
            }
            if (!userClient.existsUserId(taskDto.getAssigneeId())){
                throw new UserNotFoundException("No User found with assignee id: " + taskDto.getAssigneeId());
            }
            Task task = taskService.createTask(taskDto, activeUserId);
            return ResponseEntity.status(HttpStatus.CREATED).body(task);
        } catch (ResponseStatusException ex) {
            return ResponseEntity.status(ex.getStatusCode()).body(ex.getMessage());
        }
    }

    @PutMapping("/{id}/priority")
    public ResponseEntity<?> updateTaskPriority(@PathVariable String id, @RequestBody String priority){
        try {
            Task task = taskService.getTaskById(id);
            Task updatedTask = taskService.updateTaskPriority(task, priority);
            return ResponseEntity.status(HttpStatus.CREATED).body(updatedTask);
        } catch (ResponseStatusException ex) {
            return ResponseEntity.status(ex.getStatusCode()).body(ex.getMessage());
        }
    }

    @PutMapping("/{id}/status")
    public ResponseEntity<?> updateTaskStatus(@PathVariable String id, @RequestBody String status){
        try {
            Task task = taskService.getTaskById(id);
            Task updatedTask = taskService.updateTaskStatus(task, status);
            return ResponseEntity.status(HttpStatus.CREATED).body(updatedTask);
        } catch (ResponseStatusException ex) {
            return ResponseEntity.status(ex.getStatusCode()).body(ex.getMessage());
        }
    }

    @PutMapping("/{id}/assignee")
    public ResponseEntity<?> updateTaskAssignee(@PathVariable String id, @RequestBody Long userId){
        try {
            if (!userClient.existsUserId(userId)){
                throw new UserNotFoundException("No User found with assignee id: " + userId);
            }
            Task task = taskService.getTaskById(id);
            Task updatedTask = taskService.updateTaskAssignee(task, userId);
            return ResponseEntity.status(HttpStatus.CREATED).body(updatedTask);
        } catch (ResponseStatusException ex) {
            return ResponseEntity.status(ex.getStatusCode()).body(ex.getMessage());
        }
    }


    public Long getActiveUserId() {
        return userClient.getUserIdFromUserService();
    }

}
