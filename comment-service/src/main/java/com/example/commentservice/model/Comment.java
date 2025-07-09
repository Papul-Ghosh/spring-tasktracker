package com.example.commentservice.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "comments")
public class Comment {
    @Id
    private String id;

    @Column(nullable = false)
    private Long authorId;

    @Column(nullable = false, length = 500)
    private String content;

    @Column(nullable = false)
    private String taskId;

    @CreatedDate
    private LocalDateTime createdAt;

}