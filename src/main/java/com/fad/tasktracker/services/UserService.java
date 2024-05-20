package com.fad.tasktracker.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fad.tasktracker.entity.User;
import com.fad.tasktracker.repositories.UserRepository;

import jakarta.validation.Valid;
import jakarta.validation.ValidationException;

import java.util.Optional;

@Service
@Transactional
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    // private PasswordEncoder passwordEncoder;

    public User createUser(@Valid User user) {
        if (userRepository.findByEmail(user.getEmail()).isPresent()) {
            throw new ValidationException("Username already exists");
        }
        if (userRepository.findByEmail(user.getEmail()).isPresent()) {
            throw new ValidationException("Email already exists");
        }
        // user.setPasswordHash(passwordEncoder.encode(user.getPasswordHash()));
        return userRepository.save(user);
    }

    public Page<User> getAllUsers(int page, int size, String sortBy, String sortDirection) {
        Sort sort = sortDirection.equalsIgnoreCase(Sort.Direction.ASC.name())
                ? Sort.by(sortBy).ascending()
                : Sort.by(sortBy).descending();
        Pageable pageable = PageRequest.of(page, size, sort);
        return userRepository.findAll(pageable);
    }

    public Optional<User> getUserById(Long id) {
        return userRepository.findById(id);
    }

    public User updateUser(Long id, @Valid User user) {
        if (!userRepository.existsById(id)) {
            throw new ValidationException("User not found");
        }
        user.setId(id);
        // user.setPasswordHash(passwordEncoder.encode(user.getPasswordHash()));
        return userRepository.save(user);
    }

    public void deleteUser(Long id) {
        if (!userRepository.existsById(id)) {
            throw new ValidationException("User not found");
        }
        userRepository.deleteById(id);
    }
}
