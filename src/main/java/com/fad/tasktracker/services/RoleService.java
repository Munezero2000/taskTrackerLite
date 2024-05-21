package com.fad.tasktracker.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fad.tasktracker.entity.Role;
import com.fad.tasktracker.repositories.RoleRepository;

import jakarta.validation.Valid;
import jakarta.validation.ValidationException;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
@Transactional
public class RoleService {

    @Autowired
    private RoleRepository roleRepository;

    public Map<String, Object> createRole(@Valid Role role) {
        Map<String, Object> response = new HashMap<>();
        Optional<Role> existingRole = roleRepository.findByName(role.getName());

        if (existingRole.isPresent()) {
            response.put("message", "Role already exists");
            response.put("status", "failure");
            return response;
        }

        Role theRole = roleRepository.save(role);
        response.put("message", "Role created successfully");
        response.put("status", "success");
        response.put("role", theRole);

        return response;
    }

    public List<Role> getAllRoles() {
        return roleRepository.findAll();
    }

    public Optional<Role> getRoleByName(String name) {
        return roleRepository.findByName(name);
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
