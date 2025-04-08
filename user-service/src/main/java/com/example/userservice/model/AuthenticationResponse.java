package com.example.userservice.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AuthenticationResponse {
    @JsonProperty("access_token")
    private String accessToken;

    @JsonProperty("message")
    private String message;

    public AuthenticationResponse(String accessToken, String message) {
        this.accessToken = accessToken;
        this.message = message;
    }
}