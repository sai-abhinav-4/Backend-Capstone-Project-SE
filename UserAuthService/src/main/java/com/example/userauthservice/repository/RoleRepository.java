package com.example.userauthservice.repository;

import com.example.userauthservice.models.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RoleRepository  extends JpaRepository<Role,Long> {
    Optional<Role> findByValue(String role);
}
