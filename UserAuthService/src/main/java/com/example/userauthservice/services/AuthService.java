package com.example.userauthservice.services;

import com.example.userauthservice.clients.KafkaProducerClient;
import com.example.userauthservice.dto.EmailTemplateDto;
import com.example.userauthservice.exceptions.IncorrectPasswordException;
import com.example.userauthservice.exceptions.UserAlreadyExistException;
import com.example.userauthservice.exceptions.UserNotRegistredException;
import com.example.userauthservice.models.Role;
import com.example.userauthservice.models.Session;
import com.example.userauthservice.models.State;
import com.example.userauthservice.models.User;
import com.example.userauthservice.repository.RoleRepository;
import com.example.userauthservice.repository.SessionRepository;
import com.example.userauthservice.repository.UserRespository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtParser;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.MacAlgorithm;
import org.antlr.v4.runtime.misc.Pair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.*;

@Service
public class AuthService implements IAuthService{

    @Autowired
    private UserRespository userRespository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private SessionRepository sessionRepository;

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Autowired
    private SecretKey secretKey;

    @Autowired
    private KafkaProducerClient kafkaProducerClient;

    @Autowired
    private ObjectMapper objectMapper;

    @Override
    public User signup(String name, String email, String password) {
        Optional<User> existingUser = userRespository.findByEmail(email);
        if(existingUser.isPresent()){
            throw new UserAlreadyExistException("User already exists");
        }
        User user = new User();
        user.setName(name);
        user.setEmail(email);
        user.setPassword(bCryptPasswordEncoder.encode(password));
        user.setCreatedAt(new Date());
        user.setUpdatedAt(new Date());
        user.setState(State.ACTIVE);

        Role role =null;
        Optional<Role> roleOptional = roleRepository.findByValue("NON_ADMIN");
        if(roleOptional.isEmpty()){
            role = new Role();
            role.setValue("NON_ADMIN");
            role.setCreatedAt((new Date()));
            role.setUpdatedAt(new Date());
            role.setState(State.ACTIVE);
            roleRepository.save(role);
        }else{
            role = roleOptional.get();
        }


        List<Role> existingRoles = user.getRoles();
        existingRoles.add(role);
        user.setRoles(existingRoles);

        EmailTemplateDto emailTemplateDto = new EmailTemplateDto();
        emailTemplateDto.setTo(user.getEmail());
        emailTemplateDto.setSubject("Welcome to Scaler");
        emailTemplateDto.setBody("Welcome to Scaler, "+user.getName()+"! Your account has been successfully created.");
        emailTemplateDto.setFrom("signup@scaler.com");
        try {
            kafkaProducerClient.sendMessage("singup", objectMapper.writeValueAsString(emailTemplateDto));
        }
        catch (JsonProcessingException e){
            throw new RuntimeException("Error in sending signup email message to kafka topic"+e.getMessage());
        }
        return userRespository.save(user);
    }

    @Override
    public Pair<User,String> login(String email, String password) {
        Optional<User> userOptional = userRespository.findByEmail(email);
        if(userOptional.isEmpty()){
            throw new UserNotRegistredException("User not registred");
        }
        User user = userOptional.get();
        if(!bCryptPasswordEncoder.matches(password, user.getPassword())){
            throw  new IncorrectPasswordException("Incorrect password");
        }

        //Generate token and set to user object (to be implemented)
        Map<String, Object> claims = new HashMap<>();
        claims.put("user_id", user.getId());
        List<String> roleValues = new ArrayList<>();
        for(Role role : user.getRoles()){
            roleValues.add(role.getValue());
        }
        claims.put("access", roleValues);
        long currentTime = System.currentTimeMillis();
        claims.put("iat", currentTime);
        claims.put("exp", currentTime + 3600000); // Token valid for 1 hour
        claims.put("issuer", "UserAuthService");

        String token = Jwts.builder().claims(claims).signWith(secretKey).compact();

        Session session = new Session();
        session.setUser(user);
        session.setToken(token);
        session.setState(State.ACTIVE);
        sessionRepository.save(session);

        return new Pair<>(user,token);
    }

    // these check will happen in resource server.
    public Boolean validateToken(String token) {
        // Token validation logic to be implemented
        Optional<Session> sessionOptional = sessionRepository.findByToken(token);
        if(sessionOptional.isEmpty()){
            return false;
        }

        Session session = sessionOptional.get();
        JwtParser jwtParser = Jwts.parser().verifyWith(secretKey).build();
        Claims claims = jwtParser.parseSignedClaims(token).getPayload();

        Long expiry = (Long) claims.get("exp");
        long currentTime = System.currentTimeMillis();
        if(currentTime > expiry){
            session.setState(State.INACTIVE);
            sessionRepository.save(session);
            return false;
        }
        return true;
    }

}
