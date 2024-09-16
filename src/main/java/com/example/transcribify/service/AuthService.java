package com.example.transcribify.service;

import com.example.transcribify.model.dto.CredentialsDto;
import com.example.transcribify.model.dto.SignUpDto;
import com.example.transcribify.model.dto.UserDto;

import java.util.Optional;

public interface AuthService {
    UserDto signup(SignUpDto signUpDto);
    UserDto login(CredentialsDto credentialsDto);
    UserDto findByEmail(String email);
}
