package com.example.Net_teca.repository;

import com.example.Net_teca.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User,Long> {
    Optional<User> findByName(String name);
    boolean existsByEmail(String email);
    Optional<User> findByEmail(String email);
}
