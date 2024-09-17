package com.example.transcribify.service;

import com.example.transcribify.model.dto.CredentialsDto;
import com.example.transcribify.model.dto.SignUpDto;
import com.example.transcribify.model.dto.UserDto;

import java.io.IOException;
import java.util.Optional;

public interface AuthService {
    UserDto signup(SignUpDto signUpDto) throws IOException;
    UserDto login(CredentialsDto credentialsDto);
    UserDto findByEmail(String email);
}
