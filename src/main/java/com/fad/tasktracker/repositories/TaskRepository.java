package com.fad.tasktracker.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.fad.tasktracker.entity.Task;

@Repository
public interface TaskRepository extends JpaRepository<Task, Long> {
}
