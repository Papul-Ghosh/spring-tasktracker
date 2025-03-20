package com.example.userservice.controller;

import com.example.userservice.dto.SignupDto;
import com.example.userservice.service.UserService;
import com.example.userservice.dto.LoginDto;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/v1/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/")
    public List<SignupDto> getAllUsers(){
        return userService.findAllUsers();
    }

    @PostMapping("/signup")
    public String registerUser(@RequestBody SignupDto signupDto) {

        return userService.registerUser(signupDto);
    }

    @PostMapping("/login")
    public String login(@RequestBody LoginDto loginDto) {
        return userService.login(loginDto);
    }
}
