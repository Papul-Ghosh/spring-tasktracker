package com.example.userservice.controller;

import com.example.userservice.dto.LoginDto;
import com.example.userservice.dto.SignupDto;
import com.example.userservice.model.AuthenticationResponse;
import com.example.userservice.model.User;
import com.example.userservice.service.UserService;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

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
    @CachePut(value = "UserCache", key = "#result.id")
    public ResponseEntity<AuthenticationResponse> registerUser(@RequestBody SignupDto signupDto) {

        return ResponseEntity.ok(userService.registerUser(signupDto));
    }

    @PostMapping("/login")
    public ResponseEntity<AuthenticationResponse> login(@RequestBody LoginDto loginDto) {
        return ResponseEntity.ok(userService.login(loginDto));
    }

    @GetMapping("/activeUser")
    public ResponseEntity<?> getActiveUser() {
        try{
            User user = userService.activeUser();
            return ResponseEntity.status(HttpStatus.OK).body(user);
        }
        catch (ResponseStatusException ex){
            return ResponseEntity.status(ex.getStatusCode()).body(ex.getMessage());
        }
    }

    @GetMapping("/activeUserId")
    public ResponseEntity<?> getActiveUserId() {
        try{
            User user = userService.activeUser();
            return ResponseEntity.status(HttpStatus.OK).body(user.getId());
        }
        catch (ResponseStatusException ex){
            return ResponseEntity.status(ex.getStatusCode()).body(ex.getMessage());
        }
    }

    @GetMapping("/existsUserId/{id}")
    @Cacheable(value = "UserCache", key = "#id")
    public ResponseEntity<?> existsUserId(@PathVariable Long id) {
        try {
            return ResponseEntity.status(HttpStatus.OK).body(userService.existsUserId(id));
        } catch (ResponseStatusException ex) {
            return ResponseEntity.status(ex.getStatusCode()).body(false);
        }
    }

    @GetMapping("/{id}")
    @Cacheable(value = "UserCache", key = "#id")
    public ResponseEntity<?> getUserById(@PathVariable Long id) {
        try {
            SignupDto userDto = userService.getUserById(id);

            return ResponseEntity.status(HttpStatus.OK).body(userDto);
        } catch (ResponseStatusException ex) {
            return ResponseEntity.status(ex.getStatusCode()).body(ex.getMessage());
        }
    }

}