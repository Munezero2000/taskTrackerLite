package com.fad.tasktracker.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fad.tasktracker.entity.TaskProgress;
import com.fad.tasktracker.repositories.TaskProgressRepository;
import com.fad.tasktracker.repositories.TaskRepository;

import jakarta.validation.Valid;
import jakarta.validation.ValidationException;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class TaskProgressService {

    @Autowired
    private TaskProgressRepository taskProgressRepository;

    @Autowired
    private TaskRepository taskRepository;

    public TaskProgress createTaskProgress(@Valid TaskProgress taskProgress) {
        if (!taskRepository.existsById(taskProgress.getTask().getId())) {
            throw new ValidationException("Task not found");
        }
        return taskProgressRepository.save(taskProgress);
    }

    public List<TaskProgress> getAllTaskProgress() {
        return taskProgressRepository.findAll();
    }

    public Optional<TaskProgress> getTaskProgressById(Long id) {
        return taskProgressRepository.findById(id);
    }

    public TaskProgress updateTaskProgress(Long id, @Valid TaskProgress taskProgress) {
        if (!taskProgressRepository.existsById(id)) {
            throw new ValidationException("Task progress not found");
        }
        taskProgress.setId(id);
        return taskProgressRepository.save(taskProgress);
    }

    public void deleteTaskProgress(Long id) {
        if (!taskProgressRepository.existsById(id)) {
            throw new ValidationException("Task progress not found");
        }
        taskProgressRepository.deleteById(id);
    }
}
