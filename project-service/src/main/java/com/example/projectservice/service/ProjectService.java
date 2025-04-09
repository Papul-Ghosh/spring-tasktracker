package com.example.projectservice.service;

import com.example.projectservice.client.UserClient;
import com.example.projectservice.dto.ProjectDto;
import com.example.projectservice.dto.UserDto;
import com.example.projectservice.exception.ProjectNotFoundException;
import com.example.projectservice.model.Project;
import com.example.projectservice.model.Role;
import com.example.projectservice.repository.ProjectRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@Service
public class ProjectService {

    private final ProjectRepository projectRepository;
    private final UserClient userClient;

    public ProjectService(ProjectRepository projectRepository,
                          UserClient userClient) {
        this.projectRepository = projectRepository;
        this.userClient = userClient;
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

}