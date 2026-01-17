package com.example.userauthservice.services;

import com.example.userauthservice.models.User;

public interface IAuthService {
    User signup(String name, String email, String password);
    User login(String email, String password);
}
