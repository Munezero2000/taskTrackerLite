package com.fad.tasktracker.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fad.tasktracker.entity.Role;
import com.fad.tasktracker.repositories.RoleRepository;

import jakarta.validation.Valid;
import jakarta.validation.ValidationException;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class RoleService {

    @Autowired
    private RoleRepository roleRepository;

    public Role createRole(@Valid Role role) {
        if (roleRepository.findByName(role.getName()).isPresent()) {
            throw new ValidationException("Role already exists");
        }
        return roleRepository.save(role);
    }

    public List<Role> getAllRoles() {
        return roleRepository.findAll();
    }

    public Optional<Role> getRoleById(Long id) {
        return roleRepository.findById(id);
    }

    public Role updateRole(Long id, @Valid Role role) {
        if (!roleRepository.existsById(id)) {
            throw new ValidationException("Role not found");
        }
        role.setId(id);
        return roleRepository.save(role);
    }

    public void deleteRole(Long id) {
        if (!roleRepository.existsById(id)) {
            throw new ValidationException("Role not found");
        }
        roleRepository.deleteById(id);
    }
}
