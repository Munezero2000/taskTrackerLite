package com.fad.tasktracker.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.fad.tasktracker.dto.TaskProgressDTO;
import com.fad.tasktracker.entity.Task;
import com.fad.tasktracker.entity.TaskProgress;
import com.fad.tasktracker.services.TaskProgressService;
import com.fad.tasktracker.services.TaskService;

import jakarta.validation.Valid;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/task-progress")
public class TaskProgressController {

    @Autowired
    private TaskProgressService taskProgressService;

    @Autowired
    private TaskService taskService;

    @PostMapping
    public ResponseEntity<TaskProgress> createTaskProgress(@Valid @RequestBody TaskProgressDTO taskProgressDto) {

        TaskProgress taskProgress = new TaskProgress();
        Map<String, Object> optionalTask = taskService.getTaskById(taskProgressDto.getTaskId());
        Task task = (Task) optionalTask.get("task");

        taskProgress.setTask(task);
        taskProgress.setProgressNote(taskProgressDto.getProgressNote());

        TaskProgress createdTaskProgress = taskProgressService.createTaskProgress(taskProgress);
        return ResponseEntity.ok(createdTaskProgress);
    }

    @GetMapping
    public ResponseEntity<List<TaskProgress>> getAllTaskProgress() {
        List<TaskProgress> taskProgressList = taskProgressService.getAllTaskProgress();
        return ResponseEntity.ok(taskProgressList);
    }

    @GetMapping("/{id}")
    public ResponseEntity<TaskProgress> getTaskProgressById(@PathVariable Long id) {
        Optional<TaskProgress> taskProgress = taskProgressService.getTaskProgressById(id);
        return taskProgress.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<TaskProgress> updateTaskProgress(@PathVariable Long id,
            @Valid @RequestBody TaskProgress taskProgress) {
        TaskProgress updatedTaskProgress = taskProgressService.updateTaskProgress(id, taskProgress);
        return ResponseEntity.ok(updatedTaskProgress);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteTaskProgress(@PathVariable Long id) {
        taskProgressService.deleteTaskProgress(id);
        return new ResponseEntity("Deleted successfully", HttpStatus.OK);
    }
}
