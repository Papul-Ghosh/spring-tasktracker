package com.example.projectservice.service;

import com.example.projectservice.dto.ProjectDto;
import com.example.projectservice.model.Project;
import com.example.projectservice.repository.ProjectRepository;
import com.example.userservice.model.User;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@Service
public class ProjectService {

    private final ProjectRepository projectRepository;
    private final RestTemplate restTemplate;
    private final String activeUserUrl;

    public ProjectService(ProjectRepository projectRepository,
                          RestTemplate restTemplate,
                          @Value("${user.activeUser.url}") String activeUserUrl) {
        this.projectRepository = projectRepository;
        this.restTemplate = restTemplate;
        this.activeUserUrl = activeUserUrl;
    }


    public Project createProject(ProjectDto projectDto) {
        if (projectRepository.findByName(projectDto.getName()).isPresent()) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Project already exists");
        }
        Project newProject = mapProjectDtoToProject(projectDto);
        return projectRepository.save(newProject);
    }

    public User getActiveUser() {
        return restTemplate.getForObject(activeUserUrl, User.class);
    }

    private Project mapProjectDtoToProject(ProjectDto projectDto) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        System.out.println(projectDto.getStartDate());
        Project project = new Project();
        project.setName(projectDto.getName());
        project.setDescription(projectDto.getDescription());
        project.setStartDate(LocalDate.parse(projectDto.getStartDate(), formatter));
        project.setEndDate(LocalDate.parse(projectDto.getEndDate(), formatter));
        return project;
    }

}