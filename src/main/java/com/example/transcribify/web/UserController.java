package com.example.transcribify.web;

import com.example.transcribify.model.User;
import com.example.transcribify.model.dto.UserDto;
import com.example.transcribify.service.UserService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public List<User> getAllUsers() {
        return userService.findAll();
    }

}
