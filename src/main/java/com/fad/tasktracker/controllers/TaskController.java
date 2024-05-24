package com.fad.tasktracker.controllers;

import com.fad.tasktracker.entity.Project;
import com.fad.tasktracker.entity.Task;
import com.fad.tasktracker.entity.TaskStatus;
import com.fad.tasktracker.entity.User;
import com.fad.tasktracker.services.ProjectService;
import com.fad.tasktracker.services.TaskService;
import com.fad.tasktracker.services.UserService;
import com.fad.tasktracker.util.TaskDTO;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.mail.MessagingException;
import jakarta.validation.Valid;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/tasks")
public class TaskController {

    @Autowired
    private TaskService taskService;

    @Autowired
    private UserService userService;

    @Autowired
    private ProjectService projectService;

    @PostMapping
    public ResponseEntity<Object> createTask(@Valid @RequestBody TaskDTO taskDto) throws MessagingException {
        Task task = new Task();
        Map<String, Object> optionalUser = userService.getUserById(taskDto.getAssignedToId());
        User user = (User) optionalUser.get("user");
        System.out.println(user);

        Project project = projectService.getProjectById(taskDto.getProjectId()).get();
        task.setDescription(taskDto.getDescription());
        task.setName(taskDto.getName());
        task.setAssignedTo(user);
        task.setProject(project);
        task.setDueDate(taskDto.getDueDate());
        task.setPriority(taskDto.getPriority());

        Map<String, Object> response = taskService.createTask(task);

        if (response.containsKey("error")) {
            return ResponseEntity.badRequest().body(response);
        }
        return ResponseEntity.ok(response);
    }

    @GetMapping
    public ResponseEntity<Object> getAllTasks() {
        Map<String, Object> response = taskService.getAllTasks();
        if (response.containsKey("message")) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Object> getTaskById(@PathVariable Long id) {
        Map<String, Object> response = taskService.getTaskById(id);
        if (response.containsKey("eror")) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }
        return ResponseEntity.ok(response);
    }

    @GetMapping("/user/{id}")
    public ResponseEntity<Object> getUserTask(@PathVariable Long id) {
        Map<String, Object> response = new HashMap<>();
        List<Task> userTasks = taskService.getUserTask(id);
        response.put("Tasks", userTasks);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Object> updateTask(@PathVariable Long id, @Valid @RequestBody TaskDTO taskDto) {
        Map<String, Object> optionalUser = userService.getUserById(taskDto.getAssignedToId());
        User user = (User) optionalUser.get("user");

        Project project = projectService.getProjectById(taskDto.getProjectId()).orElse(null);
        if (project == null) {
            return ResponseEntity.badRequest().body(Map.of("message", "Project not found"));
        }

        Map<String, Object> optionalTask = taskService.getTaskById(id);
        Task task = (Task) optionalTask.get("task");

        if (task == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("message", "Task not found"));
        }

        task.setDescription(taskDto.getDescription());
        task.setName(taskDto.getName());
        task.setAssignedTo(user);
        task.setProject(project);
        task.setDueDate(taskDto.getDueDate());
        task.setPriority(taskDto.getPriority());

        Map<String, Object> response = taskService.updateTask(id, task);

        if (response.containsKey("message")) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }
        return ResponseEntity.ok(response.get("task"));
    }

    @PutMapping("/{id}/status")
    public ResponseEntity<Object> updateTaskStatus(@PathVariable Long id, @Valid @RequestBody TaskStatus status) {

        Map<String, Object> optionalTask = taskService.getTaskById(id);
        Task task = (Task) optionalTask.get("task");

        if (task == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("message", "Task not found"));
        }
        task.setStatus(status);
        Map<String, Object> response = taskService.updateTask(id, task);

        if (response.containsKey("message")) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTask(@PathVariable Long id) {
        Map<String, Object> response = taskService.deleteTask(id);
        if (response.containsKey("error")) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        return ResponseEntity.noContent().build();
    }
}
