package com.fad.tasktracker.entity;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@AllArgsConstructor
@RequiredArgsConstructor
public class AuthenticationRequest {
    @NotBlank
    @Email(message = "A valid email is required")
    private String email;

    @NotBlank
    @Size(min = 4, max = 100, message = "password must be between 4 and 100 characters")
    private String password;

}
