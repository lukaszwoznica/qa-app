package com.example.qaapp.repository;

import com.example.qaapp.models.ERole;
import com.example.qaapp.models.Role;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RoleRepository {
    Optional<Role> findByName(ERole name);
}
