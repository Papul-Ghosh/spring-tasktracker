package com.example.userservice.dto;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SignupDto
{
    @NotEmpty(message = "firstname cannot be empty")
    private String firstname;
    private Long id;
    private String lastname;

    @NotEmpty(message = "Email should not be empty")
    @Email
    private String email;

    @NotEmpty(message = "Password should not be empty")
    private String password;
    private String role;

    public @NotEmpty(message = "Role should not be empty") String getRole() {
        return role;
    }

    public void setRole(@NotEmpty(message = "Role should not be empty") String role) {
        this.role = role;
    }
}