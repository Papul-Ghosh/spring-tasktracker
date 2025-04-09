package com.example.projectservice.controller;

import com.example.projectservice.dto.ProjectDto;
import com.example.projectservice.dto.UserDto;
import com.example.projectservice.model.Project;
import com.example.projectservice.service.ProjectService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping("/v1/projects")
class ProjectController {

//    @Autowired
    private final ProjectService projectService;

    public ProjectController(ProjectService projectService) {
        this.projectService = projectService;
    }

    @PostMapping("/create")
    public ResponseEntity<?> createProject(@RequestBody ProjectDto projectDto) {
        try {
            Project createdProject = projectService.createProject(projectDto);
            return ResponseEntity.status(HttpStatus.CREATED).body(createdProject);
        } catch (ResponseStatusException ex) {
            return ResponseEntity.status(ex.getStatusCode()).body(ex.getMessage());
        }
    }

    @GetMapping("/getActiveUser")
    public ResponseEntity<UserDto> getActiveUser() {
        return ResponseEntity.ok(projectService.getActiveUser());
    }

    @GetMapping("/")
    public List<Project> getAllProjects() {
        return projectService.getAllProjects();
    }

    @GetMapping("/{id}")
    public Project getProject(@PathVariable Long id) {
        return projectService.getProjectById(id);
    }


//    @PutMapping("/{id}")
//    public ResponseEntity<Project> updateProject(@PathVariable Long id, @RequestBody Project updatedProject) {
//        return projectRepository.findById(id).map(project -> {
//            project.setName(updatedProject.getName());
//            project.setDescription(updatedProject.getDescription());
//            project.setStartDate(updatedProject.getStartDate());
//            project.setEndDate(updatedProject.getEndDate());
//            return ResponseEntity.ok(projectRepository.save(project));
//        }).orElseGet(() -> ResponseEntity.notFound().build());
//    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteProject(@PathVariable Long id) {
        try {
            return ResponseEntity.ok(projectService.deleteProject(id));
        }catch (ResponseStatusException ex){
            return ResponseEntity.status(ex.getStatusCode()).body(ex.getMessage());
        }
    }
}