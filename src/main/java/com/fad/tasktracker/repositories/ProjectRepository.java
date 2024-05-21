package com.fad.tasktracker.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.fad.tasktracker.entity.Project;

@Repository
public interface ProjectRepository extends JpaRepository<Project, Long> {
}
