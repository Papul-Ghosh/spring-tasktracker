package com.example.taskservice.service;

import com.example.taskservice.dto.TaskDto;
import com.example.taskservice.exception.ProjectNotFoundException;
import com.example.taskservice.exception.UserNotFoundException;
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
        String taskId = getTaskId(taskDto.getProjectId());
        Task newTask = mapTaskDtoToTask(taskDto, taskId, userId);
        return taskRepository.save(newTask);
    }

    public Task updateTaskPriority(Task task, String priority) {
        Priority newPriority = Priority.valueOf(priority.toUpperCase());
        task.setPriority(newPriority);
        return taskRepository.save(task);
    }

    public Task updateTaskStatus(Task task, String status) {
        System.out.println(status);
        Status newStatus = Status.valueOf(status.toUpperCase());
        task.setStatus(newStatus);
        return taskRepository.save(task);
    }

    public Task updateTaskAssignee(Task task, Long userId) {
        task.setAssigneeId(userId);
        return taskRepository.save(task);
    }


    private String getTaskId(Long projectId) {
        int nextInt = 1;
        List<Task> existingTasks = taskRepository.findTasksByProjectIdOrderByIdDesc(projectId);
        if (!existingTasks.isEmpty()) {
            String lastId = existingTasks.getFirst().getId();
            String[] parts = lastId.split("_");
            nextInt = Integer.parseInt(parts[parts.length-1]) + 1;
        }
        return "T_" + projectId + "_" + nextInt;
    }

    private Task mapTaskDtoToTask(TaskDto taskDto, String taskId, long userId) {
        Task task = new Task();
        task.setId(taskId);
        task.setTitle(taskDto.getTitle());
        task.setDescription(taskDto.getDescription());
        task.setProjectId(taskDto.getProjectId());
        task.setOwnerId(userId);
        task.setStatus(Status.OPEN);
        task.setStoryPoint(taskDto.getStoryPoint());

        if (taskDto.getAssigneeId() == null){
            task.setAssigneeId(userId);
        }
        else task.setAssigneeId(taskDto.getAssigneeId());

        if (taskDto.getPriority() == null){
            task.setPriority(Priority.LOW);
        }
        else task.setPriority(Priority.valueOf(taskDto.getPriority().toUpperCase()));



        return task;
    }

    public Task getTaskById(String id) {
        return taskRepository.findById(id)
                .orElseThrow(() -> new ProjectNotFoundException("Task not found with id: " + id));
    }
}