package com.example.taskservice.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

@FeignClient(name = "task-service", url = "${userservice.url}")
public interface UserClient {
    @GetMapping("${userservice.activeUserid.url}")
    Long getUserIdFromUserService();
}