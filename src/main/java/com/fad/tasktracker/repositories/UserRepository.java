package com.fad.tasktracker.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.fad.tasktracker.entity.User;

public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByEmail(String email);
}
