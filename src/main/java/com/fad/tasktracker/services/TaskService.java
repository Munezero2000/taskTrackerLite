package com.fad.tasktracker.services;

import com.fad.tasktracker.configuration.NotificationService;
import com.fad.tasktracker.entity.Task;
import com.fad.tasktracker.entity.User;
import com.fad.tasktracker.repositories.ProjectRepository;
import com.fad.tasktracker.repositories.TaskRepository;
import com.fad.tasktracker.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import jakarta.mail.MessagingException;
import jakarta.validation.Valid;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import javax.management.Notification;

@Service
@Transactional
public class TaskService {

    @Autowired
    private TaskRepository taskRepository;

    @Autowired
    private ProjectRepository projectRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private NotificationService notificationService;

    public Map<String, Object> createTask(@Valid Task task) throws MessagingException {
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
        notificationService.sendNotification(task.getAssignedTo().getEmail(), "Task Assignment",
                "New Task New Challange, An Opportunity for Growth",
                "Dear "+task.getAssignedTo().getFirstName()+" You have been assigned a new task of " + task.getName() + " with a dueDate of " + task.getDueDate()
                        + " View your portal and read more details to accomplish it in time",
                "");
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
        Optional<User> optionalUser = userRepository.findById(id);
        User user = null;
        if(optionalUser.isPresent()){
           user = optionalUser.get();
        }
        List<Task> userTasks = user.getTasks();
        return userTasks;
    }
}
