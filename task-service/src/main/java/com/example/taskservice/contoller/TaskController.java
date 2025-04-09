package com.example.taskservice.contoller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v1/tasks")
public class TaskController {

    @PostMapping("/create")
    public ResponseEntity<?> createTask(){
        return ResponseEntity.ok().body("Everything is working");
    }

}
