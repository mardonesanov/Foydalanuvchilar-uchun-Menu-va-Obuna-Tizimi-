package com.example.demo.repository;

import com.example.demo.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    User findByUsernameAndPhoneNumber(String username, String phoneNumber);
    Optional<User> findByUsername(String username);



}
