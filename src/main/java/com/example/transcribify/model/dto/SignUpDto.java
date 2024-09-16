package com.example.transcribify.model.dto;

import com.example.transcribify.model.enumerations.Role;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SignUpDto {

    @NotBlank(message = "First name is required")
    private String firstName;

    @NotBlank(message = "Last name is required")
    private String lastName;

    private String image;

    @NotBlank(message = "Please provide your email")
    @Pattern(regexp = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$",
            message = "Please enter a valid email")
    private String email;

    @NotBlank(message = "Please enter your password")
    @Size(min = 10, message = "Password must be at least 10 characters long")
    private String password;

    @NotNull
    private Role role;
}