package com.fad.tasktracker.dto;

import java.time.LocalDate;
import com.fad.tasktracker.entity.TaskPriority;
import com.fad.tasktracker.entity.TaskStatus;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@AllArgsConstructor
@RequiredArgsConstructor
public class TaskDTO {
    private Long id;

    @NotNull
    private Long projectId;

    @NotBlank
    private String name;

    @NotBlank
    private String description;

    @NotNull
    private Long assignedToId;

    @NotNull
    private TaskPriority priority;

    @NotNull
    private LocalDate dueDate;

}