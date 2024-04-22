package com.blog.blogappapis.services;

import com.blog.blogappapis.entities.User;
import com.blog.blogappapis.payloads.UserDto;

import java.util.List;

public interface UserService {

    UserDto createUser(UserDto user);
    UserDto updateUser(UserDto user, Integer userId);
    UserDto getUserById(Integer userId);

    List<UserDto> getAllUsers();
    void deleteUser(Integer userId);
}
