package com.example.taskservice.repository;

import com.example.taskservice.model.Task;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface TaskRepository extends JpaRepository<Task, String> {
    @Query("SELECT t FROM Task t WHERE t.projectId = :projectId ORDER BY t.id DESC")
    List<Task> findTasksByProjectIdOrderByIdDesc(@Param("projectId") Long projectId);
}
