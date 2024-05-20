package com.fad.tasktracker.entity;

import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

import jakarta.persistence.*;

@Entity
@Data
@NoArgsConstructor
public class TaskProgress {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "task_id", nullable = false)
    private Task task;

    private String progressNote;

    @Column(nullable = false)
    private LocalDateTime progressDate = LocalDateTime.now();
}
