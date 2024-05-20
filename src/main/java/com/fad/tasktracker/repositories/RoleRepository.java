package com.fad.tasktracker.repositories;


import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.fad.tasktracker.entity.Role;

public interface RoleRepository extends JpaRepository<Role, Long> {

    Optional<Role> findByName(String name);
}
