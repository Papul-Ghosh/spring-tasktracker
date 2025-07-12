package com.example.projectservice.service;

import com.example.projectservice.client.TaskClient;
import com.example.projectservice.client.UserClient;
import com.example.projectservice.dto.*;
import com.example.projectservice.exception.ProjectNotFoundException;
import com.example.projectservice.model.Project;
import com.example.projectservice.model.Role;
import com.example.projectservice.repository.ProjectRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.List;

@Service
public class ProjectService {
    @Value("${app.kafka.notification-topic}")
    private String notificationTopic;

    @Autowired
    private final UserClient userClient;
    private final TaskClient taskClient;
    private final ProjectRepository projectRepository;
    KafkaTemplate<String, ProjectEventDto> kafkaTemplate;

    public ProjectService(ProjectRepository projectRepository,
                          UserClient userClient, TaskClient taskClient,
                          KafkaTemplate<String, ProjectEventDto> kafkaTemplate) {
        this.projectRepository = projectRepository;
        this.userClient = userClient;
        this.taskClient = taskClient;
        this.kafkaTemplate = kafkaTemplate;
    }


    public Project createProject(ProjectDto projectDto) {
        if (projectRepository.findByName(projectDto.getName()).isPresent()) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Project already exists");
        }
        UserDto user;
        user = getActiveUser();
        if(!user.getRole().equals(Role.MANAGER)){
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Only Managers are allowed to create new project");
        }
        Project newProject = mapProjectDtoToProject(projectDto, user.getId());
        return projectRepository.save(newProject);
    }

    public Project getProjectById(Long id) {
        return projectRepository.findById(id)
                .orElseThrow(() -> new ProjectNotFoundException("Project not found with id: " + id));
    }


    public UserDto getActiveUser() {
        return userClient.getUserFromUserService();
    }

    private Project mapProjectDtoToProject(ProjectDto projectDto, long userId) {
        List<String> taskIds = Collections.<String>emptyList();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        Project project = new Project();
        project.setName(projectDto.getName());
        project.setDescription(projectDto.getDescription());
        project.setStartDate(LocalDate.parse(projectDto.getStartDate(), formatter));
        project.setEndDate(LocalDate.parse(projectDto.getEndDate(), formatter));
        project.setOwnerid(userId);
        project.setTaskIds(taskIds);
        return project;
    }

    public List<Project> getAllProjects() {
        return projectRepository.findAll();
    }

    public String deleteProject(Long id){
        if (!projectRepository.existsById(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Project not found");
        }

        Long ownerId = getProjectById(id).getOwnerid();
        UserDto user;
        user = getActiveUser();
        if(!user.getId().equals(ownerId)){
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Only project owner is allowed to delete the project");
        }
        projectRepository.deleteById(id);
        return "Project delete successful";
    }

    public List<TaskDto> getTasksByProjectId(Long projectId) {
        List<TaskDto> tasks = taskClient.getTasksByProjectId(projectId);
        return tasks;
    }

    public void syncNewTask(TaskProjectDto taskProjectDto){

        Project project = getProjectById(taskProjectDto.getProjectId());
        List<String> taskList = project.getTaskIds();
        taskList.add(taskProjectDto.getTaskId());
        project.setTaskIds(taskList);
        projectRepository.save(project);

        ProjectEventDto projectEventDto = SendTaskNotification(taskProjectDto, "TASK_CREATED");
        kafkaTemplate.send(notificationTopic, String.valueOf(project.getId()), projectEventDto);

    }

    public void removeTask(TaskProjectDto taskProjectDto){

        Project project = getProjectById(taskProjectDto.getProjectId());
        List<String> taskList = project.getTaskIds();
        taskList.remove(taskProjectDto.getTaskId());
        project.setTaskIds(taskList);
        projectRepository.save(project);
        ProjectEventDto projectEventDto = SendTaskNotification(taskProjectDto, "TASK_DELETED");
        kafkaTemplate.send(notificationTopic, String.valueOf(project.getId()), projectEventDto);
    }

    private ProjectEventDto SendTaskNotification(TaskProjectDto taskProjectDto, String msg) {
        Project project = getProjectById(taskProjectDto.getProjectId());
        UserDto projectOwner = userClient.getUserById(project.getOwnerid());
        UserDto newTaskOwner = userClient.getUserById(taskProjectDto.getTaskOwnerId());

        ProjectNotificationDto projectNotificationDto = new ProjectNotificationDto();
        projectNotificationDto.setEmail(projectOwner.getEmail());
        projectNotificationDto.setFullName(projectOwner.getFirstname() + " " + projectOwner.getLastname());
        projectNotificationDto.setProjectId(project.getId());
        projectNotificationDto.setProjectName(project.getName());
        projectNotificationDto.setTaskId(taskProjectDto.getTaskId());
        projectNotificationDto.setTaskName(taskProjectDto.getTaskTitle());
        projectNotificationDto.setTaskOwnerFullName(newTaskOwner.getFirstname() + " " + newTaskOwner.getLastname());
        projectNotificationDto.setUpdatedAt(LocalDateTime.now());
        ProjectEventDto projectEventDto = new ProjectEventDto(msg, projectNotificationDto);
        return projectEventDto;
    }

    @KafkaListener(topics = "#{'${app.kafka.task-topic}'}", groupId = "project-service", containerFactory = "taskProjectListener")
    public void handleTaskEvents(TaskEventDto eventDto) {
        switch (eventDto.getEventType()) {
            case "TASK_CREATED" -> syncNewTask(eventDto.getTaskProjectDto());
            case "TASK_DELETED" -> removeTask(eventDto.getTaskProjectDto());
        }
    }

}