package com.example.taskservice.client;

import com.example.taskservice.dto.CommentDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@FeignClient(name = "comment-service", url = "${commentservice.url}")
public interface CommentClient {
    @GetMapping("${commentservice.getComments.url}/{taskId}")
    List<CommentDto> getcommentsByTaskId(@PathVariable("taskId") String taskId);
}