package com.fad.tasktracker.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fad.tasktracker.entity.User;
import com.fad.tasktracker.repositories.UserRepository;

import jakarta.validation.ValidationException;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Service
@Transactional
public class UserService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public Map<String, Object> createUser(User user) {
        Map<String, Object> response = new HashMap<>();
        if (userRepository.findByEmail(user.getEmail()).isPresent()) {
            response.put("message", "This email has been taken use another email");
            return response;
        }
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        User savedUser = userRepository.save(user);

        response.put("message", "User created successfully");
        response.put("user", savedUser);
        return response;
    }

    public Map<String, Object> getAllUsers(int page, int size, String sortBy, String sortDirection) {
        Sort sort = sortDirection.equalsIgnoreCase(Sort.Direction.ASC.name())
                ? Sort.by(sortBy).ascending()
                : Sort.by(sortBy).descending();
        Pageable pageable = PageRequest.of(page, size, sort);
        Page<User> userPage = userRepository.findAll(pageable);

        Map<String, Object> response = new HashMap<>();
        response.put("users", userPage.getContent());
        response.put("currentPage", userPage.getNumber());
        response.put("totalItems", userPage.getTotalElements());
        response.put("totalPages", userPage.getTotalPages());
        return response;
    }

    public Map<String, Object> getUserById(Long id) {
        Optional<User> userOptional = userRepository.findById(id);
        Map<String, Object> response = new HashMap<>();

        if (userOptional.isEmpty()) {
            response.put("message", "User not found");
            return response;
        }

        response.put("message", "User found");
        response.put("user", userOptional.get());
        return response;
    }

    public Map<String, Object> updateUser(Long id, User user) {
        Map<String, Object> response = new HashMap<>();
        Optional<User> optionalUser = userRepository.findById(id);

        if (!optionalUser.isPresent()) {
            response.put("message", "User not found");
            return response;
        }

        User existingUser = optionalUser.get();

        // Update only the non-null fields
        if (user.getFirstName() != null) {
            existingUser.setFirstName(user.getFirstName());
        }
        if (user.getLastName() != null) {
            existingUser.setLastName(user.getLastName());
        }
        if (user.getEmail() != null) {
            existingUser.setEmail(user.getEmail());
        }
        if (user.getPassword() != null) {
            existingUser.setPassword(user.getPassword());
        }
        if (user.getGender() != null) {
            existingUser.setGender(user.getGender());
        }
        if (user.getRole() != null) {
            existingUser.setRole(user.getRole());
        }

        User updatedUser = userRepository.save(existingUser);

        response.put("message", "User updated successfully");
        response.put("user", updatedUser);
        return response;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<User> user = userRepository.findByEmail(username);
        if (user.isPresent()) {
            User theUser = user.get();
            return theUser;
        } else {
            throw new UsernameNotFoundException("User not found with username: " + username);
        }
    }

    public Map<String, Object> deleteUser(Long id) {
        if (!userRepository.existsById(id)) {
            throw new ValidationException("User not found");
        }
        userRepository.deleteById(id);

        Map<String, Object> response = new HashMap<>();
        response.put("message", "User deleted successfully");
        return response;
    }
}
