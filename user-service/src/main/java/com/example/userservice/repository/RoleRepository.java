package com.example.userservice.repository;

import com.example.userservice.model.Role;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RoleRepository extends JpaRepository<Role, Long> {
    Role findByName(String roleAdmin);

//    Optional<Role> findById(Long id);
}