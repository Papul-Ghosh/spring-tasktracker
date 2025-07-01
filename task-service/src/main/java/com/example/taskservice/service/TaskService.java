package com.example.taskservice.service;

import com.example.taskservice.dto.TaskCreatedDto;
import com.example.taskservice.dto.TaskDto;
import com.example.taskservice.exception.TaskNotFoundException;
import com.example.taskservice.model.Priority;
import com.example.taskservice.model.Status;
import com.example.taskservice.model.Task;
import com.example.taskservice.repository.TaskRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
public class TaskService {

    @Autowired
    private final TaskRepository taskRepository;
    KafkaTemplate<String, TaskCreatedDto> kafkaTemplate;

    public TaskService(TaskRepository taskRepository, KafkaTemplate<String, TaskCreatedDto> kafkaTemplate) {
        this.taskRepository = taskRepository;
        this.kafkaTemplate = kafkaTemplate;
    }

    public Task createTask(TaskDto taskDto, Long userId) {
        String taskId = getTaskId(taskDto.getProjectId());
        Task newTask = mapTaskDtoToTask(taskDto, taskId, userId);
        TaskCreatedDto taskCreatedDto = new TaskCreatedDto(newTask.getProjectId(), newTask.getId());
        kafkaTemplate.send("topic-create-task", taskCreatedDto);
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

    public String deleteTask(String id, Long activeUserId){
        if (!taskRepository.existsById(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Task not found");
        }

        Task task = getTaskById(id);
        if (!activeUserId.equals(task.getOwnerId())){
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Only task owner is allowed to delete the task");
        }
        taskRepository.deleteById(id);

        TaskCreatedDto taskProjectDto = new TaskCreatedDto(task.getProjectId(), task.getId());
        kafkaTemplate.send("topic-delete-task", taskProjectDto);
        return "Task delete successful";
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
                .orElseThrow(() -> new TaskNotFoundException("Task not found with id: " + id));
    }

    public List<Task> getTaskByProjectId(Long projectId) {
        return taskRepository.findTasksByProjectIdOrderByIdDesc(projectId);
    }
}