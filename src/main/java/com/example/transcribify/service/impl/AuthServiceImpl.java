package com.example.transcribify.service.impl;

import com.example.transcribify.mappers.UserMapper;
import com.example.transcribify.model.User;
import com.example.transcribify.model.dto.CredentialsDto;
import com.example.transcribify.model.dto.SignUpDto;
import com.example.transcribify.model.dto.UserDto;
import com.example.transcribify.model.exceptions.AppException;
import com.example.transcribify.repository.UserRepository;
import com.example.transcribify.service.AuthService;
import com.example.transcribify.service.ImageService;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Optional;

@Service
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserMapper userMapper;
    private final ImageService imageService;

    public AuthServiceImpl(UserRepository userRepository, PasswordEncoder passwordEncoder, UserMapper userMapper, ImageService imageService) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.userMapper = userMapper;
        this.imageService = imageService;
    }

    @Override
    public UserDto signup(SignUpDto signUpDto) throws IOException {
        Optional<User> optionalUser = userRepository.findByEmail(signUpDto.getEmail());

        if (optionalUser.isPresent()) {
            throw new AppException("User with that email already exists.", HttpStatus.BAD_REQUEST);
        }

        String imagePath = imageService.saveImage(signUpDto.getImage());

        signUpDto.setImage(null);
        User user = userMapper.signUpToUser(signUpDto);
        user.setPassword(passwordEncoder.encode(signUpDto.getPassword()));
        user.setImage(imagePath);

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
