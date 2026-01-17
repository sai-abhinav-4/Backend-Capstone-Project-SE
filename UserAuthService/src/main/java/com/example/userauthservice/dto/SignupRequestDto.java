package com.example.userauthservice.dto;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class SignupRequestDto {
    private String email;
    private String name;
    private String password;
}
