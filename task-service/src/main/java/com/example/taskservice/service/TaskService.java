package com.example.taskservice.service;

import com.example.taskservice.dto.TaskDto;
import com.example.taskservice.model.Priority;
import com.example.taskservice.model.Status;
import com.example.taskservice.model.Task;
import com.example.taskservice.repository.TaskRepository;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class TaskService {

    private final TaskRepository taskRepository;

    public TaskService(TaskRepository taskRepository) {
        this.taskRepository = taskRepository;
    }

    public Task createTask(TaskDto taskDto, Long userId) {
        int nextInt = 1;
        List<Task> existingTasks = taskRepository.findTopByProjectIdOrderByIdDesc(taskDto.getProjectId());
        if (!existingTasks.isEmpty()) {
            String lastId = existingTasks.getFirst().getId();
            String[] parts = lastId.split("_");
            nextInt = Integer.parseInt(parts[parts.length-1]) + 1;
        }
        String newId = "T_" + taskDto.getProjectId() + "_" + nextInt;

        Task newTask = mapTaskDtoToTask(taskDto, newId, userId);
        return taskRepository.save(newTask);
    }

    private Task mapTaskDtoToTask(TaskDto taskDto, String taskId, long userId) {
        Task task = new Task();
        task.setId(taskId);
        task.setTitle(taskDto.getTitle());
        task.setDescription(taskDto.getDescription());
        task.setProjectId(taskDto.getProjectId());
        task.setAssigneeId(userId);
        task.setOwnerId(userId);
        task.setStatus(Status.OPEN);
        task.setPriority(Priority.valueOf(taskDto.getPriority().toUpperCase()));
        task.setStoryPoint(taskDto.getStoryPoint());
        return task;
    }
}