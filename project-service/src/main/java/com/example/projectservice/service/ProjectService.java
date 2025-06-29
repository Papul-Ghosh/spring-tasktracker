package com.example.projectservice.service;

import com.example.projectservice.client.TaskClient;
import com.example.projectservice.client.UserClient;
import com.example.projectservice.dto.ProjectDto;
import com.example.projectservice.dto.TaskDto;
import com.example.projectservice.dto.UserDto;
import com.example.projectservice.exception.ProjectNotFoundException;
import com.example.projectservice.model.Project;
import com.example.projectservice.model.Role;
import com.example.projectservice.repository.ProjectRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
public class ProjectService {

    private final ProjectRepository projectRepository;
    private final UserClient userClient;
    private final TaskClient taskClient;

    public ProjectService(ProjectRepository projectRepository,
                          UserClient userClient, TaskClient taskClient) {
        this.projectRepository = projectRepository;
        this.userClient = userClient;
        this.taskClient = taskClient;
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
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        Project project = new Project();
        project.setName(projectDto.getName());
        project.setDescription(projectDto.getDescription());
        project.setStartDate(LocalDate.parse(projectDto.getStartDate(), formatter));
        project.setEndDate(LocalDate.parse(projectDto.getEndDate(), formatter));
        project.setOwnerid(userId);
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


}