package com.example.userauthservice.Utils;

import com.example.userauthservice.dto.UserDto;
import com.example.userauthservice.models.User;

public class UserDtoMapper {
    public static UserDto from(User user){
        UserDto userDto = new UserDto();
        userDto.setId(user.getId());
        userDto.setName(user.getName());
        userDto.setRoles(user.getRoles());
        userDto.setEmail(user.getEmail());
        return userDto;
    }

    public static User to(UserDto userDto){
        User user = new User();
        user.setName(userDto.getName());
        user.setEmail(userDto.getEmail());
        return user;
    }
}
