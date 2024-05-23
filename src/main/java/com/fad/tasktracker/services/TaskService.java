package com.fad.tasktracker.services;

import com.fad.tasktracker.entity.Task;
import com.fad.tasktracker.entity.User;
import com.fad.tasktracker.repositories.ProjectRepository;
import com.fad.tasktracker.repositories.TaskRepository;
import com.fad.tasktracker.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import jakarta.validation.Valid;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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

    public Map<String, Object> createTask(@Valid Task task) {
        Map<String, Object> response = new HashMap<>();
        if (!projectRepository.existsById(task.getProject().getId())) {
            response.put("error", "Project not found");
            return response;
        }
        if (!userRepository.existsById(task.getAssignedTo().getId())) {
            response.put("error", "Assigned user not found");
            return response;
        }
        Task savedTask = taskRepository.save(task);
        response.put("message", "Task created successfully");
        response.put("task", savedTask);
        return response;
    }

    public Map<String, Object> getAllTasks() {
        Map<String, Object> response = new HashMap<>();
        List<Task> tasks = taskRepository.findAll();
        response.put("tasks", tasks);
        return response;
    }

    public Map<String, Object> getTaskById(Long id) {
        Map<String, Object> response = new HashMap<>();
        Optional<Task> optionalTask = taskRepository.findById(id);
        if (optionalTask.isPresent()) {
            response.put("task", optionalTask.get());
        } else {
            response.put("error", "Task not found");
        }
        return response;
    }

    public Map<String, Object> updateTask(Long id, @Valid Task task) {
        Map<String, Object> response = new HashMap<>();
        if (!taskRepository.existsById(id)) {
            response.put("error", "Task not found");
            return response;
        }
        task.setId(id);
        Task updatedTask = taskRepository.save(task);
        response.put("message", "Task updated successfully");
        response.put("task", updatedTask);
        return response;
    }

    public Map<String, Object> deleteTask(Long id) {
        Map<String, Object> response = new HashMap<>();
        if (!taskRepository.existsById(id)) {
            response.put("message", "Task not found");
            return response;
        }
        taskRepository.deleteById(id);
        response.put("message", "Task deleted successfully");
        return response;
    }

    public List<Task> getUserTask(Long id) {
        Map<String, Object> response = new HashMap();
        response = getTaskById(id);
        User user = (User) response.get("data");
        List<Task> userTasks = new ArrayList<>();
        if (taskRepository.findByAssignedTo(user).isPresent()) {
            userTasks = taskRepository.findByAssignedTo(user).get();

        }
        ;
        return userTasks;
    }
}
