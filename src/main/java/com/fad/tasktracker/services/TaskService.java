package com.fad.tasktracker.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fad.tasktracker.entity.Task;
import com.fad.tasktracker.repositories.ProjectRepository;
import com.fad.tasktracker.repositories.TaskRepository;
import com.fad.tasktracker.repositories.UserRepository;

import jakarta.validation.Valid;
import jakarta.validation.ValidationException;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class TaskService {

    @Autowired
    private TaskRepository taskRepository;

    @Autowired
    private ProjectRepository projectRepository;

    @Autowired
    private UserRepository userRepository;

    public Task createTask(@Valid Task task) {
        if (!projectRepository.existsById(task.getProject().getId())) {
            throw new ValidationException("Project not found");
        }
        if (!userRepository.existsById(task.getAssignedTo().getId())) {
            throw new ValidationException("Assigned user not found");
        }
        return taskRepository.save(task);
    }

    public List<Task> getAllTasks() {
        return taskRepository.findAll();
    }

    public Optional<Task> getTaskById(Long id) {
        return taskRepository.findById(id);
    }

    public Task updateTask(Long id, @Valid Task task) {
        if (!taskRepository.existsById(id)) {
            throw new ValidationException("Task not found");
        }
        task.setId(id);
        return taskRepository.save(task);
    }

    public void deleteTask(Long id) {
        if (!taskRepository.existsById(id)) {
            throw new ValidationException("Task not found");
        }
        taskRepository.deleteById(id);
    }
}
