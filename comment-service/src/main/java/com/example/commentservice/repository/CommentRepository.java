package com.example.commentservice.repository;

import com.example.commentservice.model.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentRepository extends JpaRepository<Comment, String> {
    long countByTaskId(String taskId);

}
