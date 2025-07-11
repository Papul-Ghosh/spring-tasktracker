package com.example.notificationservice.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class NotificationService {

    @Autowired
//    private final UserClient userClient;

    public NotificationService(
//            UserClient userClient,
            ) {
//        this.userClient = userClient;
    }

//    public Comment createComment(CommentDto commentDto, Long userId) {
//
//        Comment comment = new Comment();
//        comment.setAuthorId(userId);
//        comment.setTaskId(commentDto.getTaskId());
//        comment.setContent(commentDto.getContent());
//        comment.setCreatedAt(LocalDateTime.now());
//        Long count = commentRepository.countByTaskId(commentDto.getTaskId());
//        String commentId = commentDto.getTaskId() + '_' + String.valueOf(count);
//        comment.setId(commentId);
//        commentRepository.save(comment);
//        return comment;
//    }
//
//    public Long getActiveUserId() {
//        return userClient.getUserIdFromUserService();
//    }
//
//    public Comment getCommentById(String id) {
//        return commentRepository.findById(id)
//                .orElseThrow(() -> new CommentNotFoundException("Comment not found with id: " + id));
//    }
//
//    public List<Comment> getCommentsByTaskId(String taskId) {
//        return commentRepository.findCommentsByTaskIdOrderByTimeDesc(taskId);
//    }
}
