package com.example.commentservice.service;

import com.example.commentservice.client.UserClient;
import com.example.commentservice.dto.CommentDto;
import com.example.commentservice.exception.CommentNotFoundException;
import com.example.commentservice.model.Comment;
import com.example.commentservice.repository.CommentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class CommentService {

    @Autowired
    private final CommentRepository commentRepository;
    private final UserClient userClient;

    public CommentService(CommentRepository commentRepository, UserClient userClient) {
        this.commentRepository = commentRepository;
        this.userClient = userClient;
    }

    public Comment createComment(CommentDto commentDto, Long userId) {

        Comment comment = new Comment();
        comment.setAuthorId(userId);
        comment.setTaskId(commentDto.getTaskId());
        comment.setContent(commentDto.getContent());
        comment.setCreatedAt(LocalDateTime.now());
        Long count = commentRepository.countByTaskId(commentDto.getTaskId());
        String commentId = commentDto.getTaskId() + '_' + String.valueOf(count);
        comment.setId(commentId);
        commentRepository.save(comment);
        return comment;
    }

    public Long getActiveUserId() {
        return userClient.getUserIdFromUserService();
    }

    public Comment getCommentById(String id) {
        return commentRepository.findById(id)
                .orElseThrow(() -> new CommentNotFoundException("Comment not found with id: " + id));
    }

    public List<Comment> getCommentsByTaskId(String taskId) {
        return commentRepository.findCommentsByTaskIdOrderByTimeDesc(taskId);
    }
}
