package com.fad.tasktracker.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.fad.tasktracker.entity.TaskProgress;

public interface TaskProgressRepository extends JpaRepository<TaskProgress, Long> {
}
