package com.example.transcribify.mappers;

import com.example.transcribify.model.User;
import com.example.transcribify.model.dto.SignUpDto;
import com.example.transcribify.model.dto.UserDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface UserMapper {

    UserDto toUserDto(User user);

    @Mapping(target = "password", ignore = true)
    User signUpToUser(SignUpDto signUpDto);

}