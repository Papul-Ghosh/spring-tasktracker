package com.example.commentservice.repository;

import com.example.commentservice.model.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, String> {
    long countByTaskId(String taskId);

    @Query("SELECT c FROM Comment c WHERE c.taskId = :taskId ORDER BY c.createdAt DESC")
    List<Comment> findCommentsByTaskIdOrderByTimeDesc(@Param("taskId") String taskId);
}
