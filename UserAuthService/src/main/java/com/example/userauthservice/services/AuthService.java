package com.example.userauthservice.services;

import com.example.userauthservice.exceptions.IncorrectPasswordException;
import com.example.userauthservice.exceptions.UserAlreadyExistException;
import com.example.userauthservice.exceptions.UserNotRegistredException;
import com.example.userauthservice.models.Role;
import com.example.userauthservice.models.State;
import com.example.userauthservice.models.User;
import com.example.userauthservice.repository.RoleRepository;
import com.example.userauthservice.repository.UserRespository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class AuthService implements IAuthService{

    @Autowired
    private UserRespository userRespository;

    @Autowired
    private RoleRepository roleRepository;

    @Override
    public User signup(String name, String email, String password) {
        Optional<User> existingUser = userRespository.findByEmail(email);
        if(existingUser.isPresent()){
            throw new UserAlreadyExistException("User already exists");
        }
        User user = new User();
        user.setName(name);
        user.setEmail(email);
        user.setPassword(password);
        user.setCreatedAt(new Date());
        user.setUpdatedAt(new Date());
        user.setState(State.ACTIVE);

        Optional<Role> roleOptional = roleRepository.findByValue("NON_ADMIN");
        if(roleOptional.isEmpty()){
            Role role = new Role();
            role.setValue("NON_ADMIN");
            role.setCreatedAt((new Date()));
            role.setUpdatedAt(new Date());
            role.setState(State.ACTIVE);
            roleRepository.save(role);
        }

        Role role = roleRepository.findByValue("NON_ADMIN").get();
        List<Role> existingRoles = user.getRoles();
        existingRoles.add(role);
        user.setRoles(existingRoles);

        return userRespository.save(user);
    }

    @Override
    public User login(String email, String password) {
        Optional<User> userOptional = userRespository.findByEmail(email);
        if(userOptional.isEmpty()){
            throw new UserNotRegistredException("User not registred");
        }
        User user = userOptional.get();
        if(!password.equals(user.getPassword())){
            throw  new IncorrectPasswordException("Incorrect password");
        }

        return user;
    }
}
