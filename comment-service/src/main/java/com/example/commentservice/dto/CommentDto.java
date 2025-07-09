package com.example.commentservice.dto;

import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CommentDto {

    @NotEmpty(message = "content cannot be empty")
    private String content;

    @NotEmpty(message = "task id cannot be empty")
    private String taskId;

}