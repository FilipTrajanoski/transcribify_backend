package com.example.transcribify.model.dto;

import com.example.transcribify.model.enumerations.Role;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserDto {

    private String firstName;
    private String lastName;
    private String image;
    private String email;
    private Role role;
    private String token;
}
