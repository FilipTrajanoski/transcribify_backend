package com.example.transcribify.service.impl;

import com.example.transcribify.mappers.UserMapper;
import com.example.transcribify.model.User;
import com.example.transcribify.model.dto.CredentialsDto;
import com.example.transcribify.model.dto.SignUpDto;
import com.example.transcribify.model.dto.UserDto;
import com.example.transcribify.model.exceptions.AppException;
import com.example.transcribify.repository.UserRepository;
import com.example.transcribify.service.AuthService;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserMapper userMapper;

    public AuthServiceImpl(UserRepository userRepository, PasswordEncoder passwordEncoder, UserMapper userMapper) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.userMapper = userMapper;
    }

    @Override
    public UserDto signup(SignUpDto signUpDto) {
        Optional<User> optionalUser = userRepository.findByEmail(signUpDto.getEmail());

        if (optionalUser.isPresent()) {
            throw new AppException("User with that email already exists.", HttpStatus.BAD_REQUEST);
        }

        User user = userMapper.signUpToUser(signUpDto);
        user.setPassword(passwordEncoder.encode(signUpDto.getPassword()));

        User savedUser = userRepository.save(user);

        return userMapper.toUserDto(savedUser);
    }

    @Override
    public UserDto login(CredentialsDto credentialsDto) {
        User user = userRepository.findByEmail(credentialsDto.getEmail())
                .orElseThrow(() -> new AppException("User with that email does not exist.", HttpStatus.NOT_FOUND));

        if (!passwordEncoder.matches(credentialsDto.getPassword(), user.getPassword())) {
            throw new AppException("Invalid password", HttpStatus.BAD_REQUEST);
        }

        return userMapper.toUserDto(user);
    }

    @Override
    public UserDto findByEmail(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new AppException("User with that email does not exist.", HttpStatus.NOT_FOUND));

        return userMapper.toUserDto(user);
    }
}
