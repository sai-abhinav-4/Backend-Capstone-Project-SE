package com.example.userauthservice.services;


import com.example.userauthservice.exceptions.UserAlreadyExistException;
import com.example.userauthservice.exceptions.UserNotRegistredException;
import com.example.userauthservice.models.User;
import com.example.userauthservice.repository.RoleRepository;
import com.example.userauthservice.repository.UserRespository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {

    @Autowired
    private UserRespository userRespository;;
    @Autowired
    private RoleRepository roleRepository;


    public User getUserById(Long id){

        Optional<User> user = userRespository.findById(id);
        if(user.isEmpty()){
            throw new UserNotRegistredException("User with id " + id + " is not registred");
        }

        return user.get();
    }


    public User createUser(User user){
        Optional<User> existingUser = userRespository.findByEmail(user.getEmail());
        if(existingUser.isPresent()){
            throw  new UserAlreadyExistException("User with id " + user.getId() + " already exists");
        }
        return userRespository.save(user);
    }

}
