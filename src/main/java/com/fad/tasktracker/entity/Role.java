package com.fad.tasktracker.entity;

import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.*;
import jakarta.persistence.Table;
import jakarta.validation.constraints.*;

@Data
@NoArgsConstructor
@Entity
@Table(name = "user_roles")
public class Role {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Column(nullable = false, unique = true)
    private String name;

    @Column(nullable = false)
    private String description;

    @JsonIgnore
    @OneToMany(mappedBy = "role")
    private Set<User> users;
}
