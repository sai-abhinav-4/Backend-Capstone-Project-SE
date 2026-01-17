package com.example.userauthservice.models;


import jakarta.persistence.Entity;
import jakarta.persistence.ManyToMany;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Entity
public class User extends BaseModel{
    private String email;
    private String password;
    private String name;
    @ManyToMany
    private List<Role> roles = new ArrayList<>();
}
