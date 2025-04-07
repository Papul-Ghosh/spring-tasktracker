package com.example.projectservice.client;

import com.example.projectservice.dto.UserDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

@FeignClient(name = "project-service", url = "${userservice.url}")
public interface UserClient {
    @GetMapping("${userservice.activeUser.url}")
    UserDto getUserFromUserService();
}