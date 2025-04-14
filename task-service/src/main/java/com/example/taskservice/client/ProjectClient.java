package com.example.taskservice.client;

import jakarta.validation.constraints.NotEmpty;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "project-service", url = "${projectservice.url}")
public interface ProjectClient {
    @GetMapping("${projectservice.projectid.url}/{projectId}")
    boolean existsProjectId(@PathVariable("projectId") Long projectId);
}