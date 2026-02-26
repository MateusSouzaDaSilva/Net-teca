package com.example.Net_teca.repository;

import com.example.Net_teca.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User,Long> {
    User findByName(String name);
    boolean existsByEmail(String email);
}
