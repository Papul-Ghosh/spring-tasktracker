package com.example.notificationservice.controller;

import com.example.notificationservice.service.NotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping("/v1/notifications")
public class NotificationController {
    @Autowired
    private final NotificationService notificationService;

    public NotificationController(NotificationService notificationService) {
        this.notificationService = notificationService;
    }

    @PostMapping("/test")
    public ResponseEntity<?> createComment(@RequestBody String str) {
        try {
//            Long activeUserId = commentService.getActiveUserId();
//            Comment comment = commentService.createComment(commentDto, activeUserId);
            return ResponseEntity.status(HttpStatus.CREATED).body(str);
        } catch (ResponseStatusException ex) {
            return ResponseEntity.status(ex.getStatusCode()).body(ex.getMessage());
        }
    }
//
//    @GetMapping("/{id}")
//    public Comment getComment(@PathVariable String id) {
//        return commentService.getCommentById(id);
//    }
//
//    @GetMapping("/task/{taskId}")
//    public List<Comment> getCommentsByTask(@PathVariable String taskId) {
//        return commentService.getCommentsByTaskId(taskId);
//    }

}
