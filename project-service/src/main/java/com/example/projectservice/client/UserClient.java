package com.example.projectservice.client;

import com.example.projectservice.dto.UserDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "user-service", url = "${userservice.url}")
public interface UserClient {
    @GetMapping("${userservice.activeUser.url}")
    UserDto getUserFromUserService();
    @GetMapping("/{userId}")
    UserDto getUserById(@PathVariable ("userId") Long userId);
}