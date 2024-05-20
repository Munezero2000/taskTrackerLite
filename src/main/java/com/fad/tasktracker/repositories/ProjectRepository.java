package com.fad.tasktracker.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.fad.tasktracker.entity.Project;

public interface ProjectRepository extends JpaRepository<Project, Long> {
}
