package com.fad.tasktracker.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.fad.tasktracker.entity.Task;

public interface TaskRepository extends JpaRepository<Task, Long> {
}
