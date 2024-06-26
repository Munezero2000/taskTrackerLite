package com.fad.tasktracker.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.fad.tasktracker.entity.TaskProgress;

@Repository
public interface TaskProgressRepository extends JpaRepository<TaskProgress, Long> {
}
