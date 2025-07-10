package com.example.taskservice.dto;

import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CommentDto {
    @NotEmpty(message = "Comment id cannot be empty")
    private String id;

    @NotEmpty(message = "author id cannot be empty")
    private Long authorId;

    @NotEmpty(message = "content cannot be empty")
    private String content;

    @NotEmpty(message = "time of comment creation cannot be empty")
    private LocalDateTime createdAt;
}