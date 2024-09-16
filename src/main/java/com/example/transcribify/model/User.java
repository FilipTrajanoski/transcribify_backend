package com.example.transcribify.model;

import com.example.transcribify.model.enumerations.Role;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Document
public class User {

    @NotBlank(message = "First name is required")
    private String firstName;

    @NotBlank(message = "Last name is required")
    private String lastName;

    private String image;

    @NotBlank(message = "Please provide your email")
    @Pattern(regexp = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$",
            message = "Please enter a valid email")
    @Indexed(unique = true)
    private String email;

    @NotBlank(message = "Please enter your password")
    @Size(min = 10, message = "Password must be at least 10 characters long")
    private String password;

    @NotNull
    private Role role;
}
