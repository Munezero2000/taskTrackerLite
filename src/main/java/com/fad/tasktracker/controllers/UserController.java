package com.fad.tasktracker.controllers;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import com.fad.tasktracker.entity.Role;
import com.fad.tasktracker.entity.User;
import com.fad.tasktracker.services.RoleService;
import com.fad.tasktracker.services.UserService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/users")
@Validated
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private RoleService roleService;

    @PostMapping("/")
    public ResponseEntity<Map<String, Object>> createUser(@Valid @RequestBody User user) {
        User theUser = user;
        theUser.setGender(user.getGender());
        System.out.println("I am checking user password: "+ user.getPassword());
        Optional<Role> optionalRole = roleService.getRoleByName("DEVELOPER");
        if (optionalRole.isPresent()) {
            Role role = optionalRole.get();
            theUser.setRole(role);
        }
        Map<String, Object> response = userService.createUser(theUser);
        if (response.get("message").equals("This email has been taken use another email")) {
            return ResponseEntity.badRequest().body(response);
        } else {
            return ResponseEntity.ok(response);
        }

    }

    @GetMapping("/")
    public ResponseEntity<Map<String, Object>> getAllUsers(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "asc") String sortDirection) {
        Map<String, Object> response = userService.getAllUsers(page, size, sortBy, sortDirection);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Map<String, Object>> getUserById(@PathVariable Long id) {
        try {

            Map<String, Object> response = userService.getUserById(id);
            if (response.get("message").equals("User not found")) {
                return ResponseEntity.badRequest().body(response);
            } else {
                return ResponseEntity.ok(response);
            }
        } catch (Exception e) {
            e.getMessage();
            Map<String, Object> responseBody = new HashMap();
            responseBody.put("Error", "Internal server error");
            return ResponseEntity.internalServerError().body(responseBody);
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<Map<String, Object>> updateUser(@PathVariable Long id, @Valid @RequestBody User user) {
        Map<String, Object> response = userService.updateUser(id, user);
        if (response.get("message").equals("User not found")) {
            return ResponseEntity.badRequest().body(response);
        } else {
            return ResponseEntity.ok(response);
        }

    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, Object>> deleteUser(@PathVariable Long id) {
        Map<String, Object> response = userService.deleteUser(id);
        return ResponseEntity.ok(response);
    }
}
