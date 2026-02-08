package com.example.userauthservice.controller;

import com.example.userauthservice.dto.LoginRequestDto;
import com.example.userauthservice.dto.SignupRequestDto;
import com.example.userauthservice.dto.UserDto;
import com.example.userauthservice.dto.ValidateTokenDto;
import com.example.userauthservice.models.User;
import com.example.userauthservice.services.IAuthService;
import org.antlr.v4.runtime.misc.Pair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private IAuthService authService;


    //signup
    @PostMapping("/signup")
    public ResponseEntity<UserDto> signup(@RequestBody SignupRequestDto signupRequestDto){
        User user = authService.signup(signupRequestDto.getName(), signupRequestDto.getEmail(), signupRequestDto.getPassword());
        return  new ResponseEntity<>(from(user), HttpStatus.CREATED);
    }

    //login
    @PostMapping("/login")
    public ResponseEntity<UserDto> login( @RequestBody LoginRequestDto loginRequestDto){
        Pair<User,String> userResponse = authService.login( loginRequestDto.getEmail(), loginRequestDto.getPassword());

        MultiValueMap<String,String> header = new LinkedMultiValueMap<>();
        header.add("Set-Cookie", userResponse.b);
        User user = userResponse.a;
        return new ResponseEntity<>(from(user), header, HttpStatus.OK);
    }

    @PostMapping("/validateToken")
    public ResponseEntity<Void> validateToken(@RequestBody ValidateTokenDto validateTokenDto){
       Boolean result = authService.validateToken(validateTokenDto.getToken());
         if(result){
            return new ResponseEntity<>(HttpStatus.OK);
         }else{
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
         }
    }

    private UserDto from(User user){
        UserDto userDto = new UserDto();
        userDto.setId(user.getId());
        userDto.setName(user.getName());
        userDto.setRoles(user.getRoles());
        userDto.setEmail(user.getEmail());
        return userDto;
    }
}
