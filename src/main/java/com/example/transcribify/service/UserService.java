package com.example.transcribify.service;

import com.example.transcribify.model.User;
import com.example.transcribify.model.dto.UserDto;
import com.example.transcribify.model.enumerations.Role;

import java.util.List;
import java.util.Optional;

public interface UserService{

    User findById(String id);

    List<User> findAll();

    Optional<User> updateUser(String id, UserDto userDto);

    void deleteUser(String id);

}
