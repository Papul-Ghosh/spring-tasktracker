package com.example.commentservice.controller;

import com.example.commentservice.dto.CommentDto;
import com.example.commentservice.model.Comment;
import com.example.commentservice.service.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping("/v1/comments")
public class CommentController {
    @Autowired
    private final CommentService commentService;

    public CommentController(CommentService commentService) {
        this.commentService = commentService;
    }

    @PostMapping("/create")
    public ResponseEntity<?> createComment(@RequestBody CommentDto commentDto) {
        try {
            Long activeUserId = commentService.getActiveUserId();
            Comment comment = commentService.createComment(commentDto, activeUserId);
            return ResponseEntity.status(HttpStatus.CREATED).body(comment);
        } catch (ResponseStatusException ex) {
            return ResponseEntity.status(ex.getStatusCode()).body(ex.getMessage());
        }
    }

    @GetMapping("/{id}")
    public Comment getComment(@PathVariable String id) {
        return commentService.getCommentById(id);
    }

    @GetMapping("/task/{taskId}")
    public List<Comment> getCommentsByTask(@PathVariable String taskId) {
        return commentService.getCommentsByTaskId(taskId);
    }

}
