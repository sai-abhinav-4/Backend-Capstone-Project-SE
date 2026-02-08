package com.example.userauthservice.controller;

import com.example.userauthservice.Utils.UserDtoMapper;
import com.example.userauthservice.dto.UserDto;
import com.example.userauthservice.models.User;
import com.example.userauthservice.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping("/{id}")
    public UserDto getUserById(@PathVariable Long userId) {
        User user = userService.getUserById(userId);
        return UserDtoMapper.from(user);
    }


    @PostMapping
    public UserDto createUser(@RequestBody UserDto userDto) {
        User input = UserDtoMapper.to(userDto);
        User user = userService.createUser(input);
        return UserDtoMapper.from(user);
    }
}
