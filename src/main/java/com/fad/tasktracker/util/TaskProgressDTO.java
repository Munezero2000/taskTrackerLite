package com.fad.tasktracker.util;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@AllArgsConstructor
@RequiredArgsConstructor
public class TaskProgressDTO {

    @NotNull
    private Long taskId;

    @NotBlank
    @Size(min = 10, max = 1024)
    private String progressNote;
}
