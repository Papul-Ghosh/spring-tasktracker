package com.example.userservice.controller;

import com.example.userservice.dto.AuthenticationResponse;
import com.example.userservice.model.User;
import com.example.userservice.security.JwtUtil;
import com.example.userservice.service.UserService;
import com.example.userservice.dto.LoginRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/v1/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/signup")
    public String registerUser(@RequestBody User user) {
        return userService.registerUser(user);
    }

    @PostMapping("/login")
    public String login(@RequestParam String username, @RequestParam String password) {
        return userService.login(username, password);
    }
}
