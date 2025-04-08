package com.example.userservice.service;

import com.example.userservice.dto.LoginDto;
import com.example.userservice.dto.SignupDto;
import com.example.userservice.model.AuthenticationResponse;
import com.example.userservice.model.Role;
import com.example.userservice.model.Token;
import com.example.userservice.model.User;
import com.example.userservice.repository.TokenRepository;
import com.example.userservice.repository.UserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final TokenRepository tokenRepository;
    private final AuthenticationManager authenticationManager;

    public UserService(UserRepository userRepository,
                       PasswordEncoder passwordEncoder, JwtService jwtService,
                       TokenRepository tokenRepository, AuthenticationManager authenticationManager) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
        this.tokenRepository = tokenRepository;
        this.authenticationManager = authenticationManager;
    }

    public AuthenticationResponse registerUser(SignupDto signupDto) {
        if(userRepository.findByEmail(signupDto.getEmail()).isPresent()) {
            return new AuthenticationResponse(null,"User already exist");
        }
        User user = mapToUser(signupDto);

        if(signupDto.getRole() == null){
            user.setRole(Role.DEVELOPER);
        }
        else user.setRole(Role.valueOf(signupDto.getRole()));

        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user = userRepository.save(user);

        String accessToken = jwtService.generateAccessToken(user);

        saveUserToken(accessToken, user);

        return new AuthenticationResponse(accessToken, "User registration was successful");
    }

    public AuthenticationResponse login(LoginDto loginDto) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginDto.getEmail(),
                        loginDto.getPassword()
                )
        );
        User user = userRepository.findByEmail(loginDto.getEmail()).orElseThrow();

        String accessToken = jwtService.generateAccessToken(user);

        revokeAllTokenByUser(user);
        saveUserToken(accessToken, user);

        return new AuthenticationResponse(accessToken, "User login was successful");
    }

    public List<SignupDto> findAllUsers() {
        List<User> users = userRepository.findAll();
        return users.stream()
                .map(this::mapToUserDto)
                .collect(Collectors.toList());
    }

    private SignupDto mapToUserDto(User user){
        SignupDto userDto = new SignupDto();
        userDto.setFirstname(user.getFirstname());
        userDto.setLastname(user.getLastname());
        userDto.setEmail(user.getEmail());
        return userDto;
    }

    private User mapToUser(SignupDto signupDto){
        User user = new User();
        user.setFirstname(signupDto.getFirstname());
        user.setLastname(signupDto.getLastname());
        user.setEmail(signupDto.getEmail());
        user.setPassword(passwordEncoder.encode(signupDto.getPassword()));
        return user;
    }

    private void saveUserToken(String accessToken, User user) {
        Token token = new Token();
        token.setAccessToken(accessToken);
        token.setLoggedOut(false);
        token.setUser(user);
        tokenRepository.save(token);
    }

    private void revokeAllTokenByUser(User user) {
        List<Token> validTokens = tokenRepository.findAllAccessTokensByUser(user.getId());
        if(validTokens.isEmpty()) {
            return;
        }

        validTokens.forEach(t-> {
            t.setLoggedOut(true);
        });

        tokenRepository.saveAll(validTokens);
    }


    public User activeUser(){
        List<Token> activeTokens = tokenRepository.findByLoggedOut(false);
        if(activeTokens.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "No logged in user");
        }
        if(activeTokens.size()> 1) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Multiple user logged in");
        }
        return activeTokens.getFirst().getUser();
    }






}
