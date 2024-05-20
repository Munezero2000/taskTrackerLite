package com.fad.tasktracker.entity;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;

@Entity
@Data
@NoArgsConstructor
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Size(min = 2, max = 50)
    @Column(name = "first_name", nullable = false, unique = true)
    private String firstName;

    @NotBlank
    @Size(min = 2, max = 50)
    @Column(name = "last_name", nullable = false, unique = true)
    private String lastName;

    @NotBlank(message = "Email is required")
    @Email(message = "invalid email format")
    @Column(nullable = false, unique = true)
    private String email;

    @NotBlank
    @Size(min = 8)
    @Column(nullable = false)
    private String password;

    @NotBlank
    @Column(name = "gender")
    private String gender;

    @ManyToOne
    @JoinColumn(name = "role_id", nullable = false)
    private Role role;

    @OneToMany(mappedBy = "createdBy")
    private List<Project> projects;

    @OneToMany(mappedBy = "assignedTo")
    private List<Task> tasks;
}
