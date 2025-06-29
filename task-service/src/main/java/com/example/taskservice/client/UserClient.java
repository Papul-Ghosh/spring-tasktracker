package com.example.taskservice.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "user-service", url = "${userservice.url}")
public interface UserClient {
    @GetMapping("${userservice.activeUserid.url}")
    Long getUserIdFromUserService();
    @GetMapping("${userservice.existsUserId.url}/{userId}")
    boolean existsUserId(@PathVariable("userId") Long userId);
}