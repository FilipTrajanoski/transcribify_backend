package com.example.transcribify.service.impl;

import com.example.transcribify.model.User;
import com.example.transcribify.model.dto.UserDto;
import com.example.transcribify.model.enumerations.Role;
import com.example.transcribify.model.exceptions.UserNotFoundException;
import com.example.transcribify.repository.UserRepository;
import com.example.transcribify.service.UserService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public User findById(String id) {
        return userRepository.findById(id).orElseThrow(() -> new UserNotFoundException(id));
    }

    @Override
    public List<User> findAll() {
        return userRepository.findAll();
    }

    @Override
    public Optional<User> updateUser(String id, UserDto userDto) {
        User user = this.findById(id);

        user.setRole(userDto.getRole());

        userRepository.save(user);
        return Optional.of(user);
    }

    @Override
    public void deleteUser(String id) {
        userRepository.deleteById(id);
    }
}
