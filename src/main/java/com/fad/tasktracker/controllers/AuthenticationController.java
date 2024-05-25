package com.fad.tasktracker.controllers;

import com.fad.tasktracker.configuration.JwtUtil;
import com.fad.tasktracker.entity.Role;
import com.fad.tasktracker.entity.User;
import com.fad.tasktracker.services.RoleService;
import com.fad.tasktracker.services.UserService;
import com.fad.tasktracker.util.AuthenticationRequest;

import jakarta.mail.MessagingException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@Validated
public class AuthenticationController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private UserService userService;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private RoleService roleService;

    // add a function to register user here
    @PostMapping("/register")
    public ResponseEntity<Map<String, Object>> createUser(@Valid @RequestBody User user) throws MessagingException {
        User theUser = user;
        theUser.setGender(user.getGender());

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

    @PostMapping("/login")
    public ResponseEntity<?> createAuthenticationToken(
            @Valid @RequestBody AuthenticationRequest authenticationRequest,
            HttpServletResponse response) throws Exception {
        // Authenticate the user
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(authenticationRequest.getEmail(),
                        authenticationRequest.getPassword()));
        SecurityContextHolder.getContext().setAuthentication(authentication);

        // Load user details and create JWT token
        final UserDetails userDetails = userService.loadUserByUsername(authenticationRequest.getEmail());
        final String jwt = jwtUtil.createToken(userDetails);

        // Set JWT in a cookie
        Cookie cookie = new Cookie("token", jwt);
        cookie.setHttpOnly(true);
        cookie.setSecure(false);
        cookie.setPath("/");
        cookie.setMaxAge(7 * 24 * 60 * 60);
        response.addCookie(cookie);

        Map<String, Object> responseBody = new HashMap();
        responseBody.put("message", "Login Successful");
        responseBody.put("userId", userService.findUserByEmail(userDetails.getUsername()).getId());

        return new ResponseEntity<>(responseBody, HttpStatus.OK);
    }

}
