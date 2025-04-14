package com.example.taskservice.contoller;

import com.example.taskservice.client.UserClient;
import com.example.taskservice.dto.TaskDto;
import com.example.taskservice.model.Task;
import com.example.taskservice.service.TaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/v1/tasks")
public class TaskController {

    @Autowired
    private final TaskService taskService;
    private final UserClient userClient;

    public TaskController(TaskService taskService, UserClient userClient) {
        this.taskService = taskService;
        this.userClient = userClient;
    }

    @PostMapping("/create")
    public ResponseEntity<?> createTask(@RequestBody TaskDto taskDto){
        try {
            Long activeUserId = getActiveUserId();
            Task task = taskService.createTask(taskDto, activeUserId);
            return ResponseEntity.status(HttpStatus.CREATED).body(task);
        } catch (ResponseStatusException ex) {
            return ResponseEntity.status(ex.getStatusCode()).body(ex.getMessage());
        }
    }




    public Long getActiveUserId() {
        return userClient.getUserIdFromUserService();
    }

}
