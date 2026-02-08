package com.example.userauthservice.services;

import com.example.userauthservice.models.User;
import org.antlr.v4.runtime.misc.Pair;

public interface IAuthService {
    User signup(String name, String email, String password);
    Pair<User,String> login(String email, String password);
    Boolean validateToken(String token);
}
