package com.fad.tasktracker.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fad.tasktracker.entity.Project;
import com.fad.tasktracker.repositories.ProjectRepository;
import com.fad.tasktracker.repositories.UserRepository;

import jakarta.validation.Valid;
import jakarta.validation.ValidationException;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class ProjectService {

    @Autowired
    private ProjectRepository projectRepository;

    @Autowired
    private UserRepository userRepository;

    public Project createProject(@Valid Project project) {
        if (!userRepository.existsById(project.getCreatedBy().getId())) {
            throw new ValidationException("Creator user not found");
        }
        return projectRepository.save(project);
    }

    public List<Project> getAllProjects() {
        return projectRepository.findAll();
    }

    public Optional<Project> getProjectById(Long id) {
        return projectRepository.findById(id);
    }

    public Project updateProject(Long id, @Valid Project project) {
        if (!projectRepository.existsById(id)) {
            throw new ValidationException("Project not found");
        }
        project.setId(id);
        return projectRepository.save(project);
    }

    public void deleteProject(Long id) {
        if (!projectRepository.existsById(id)) {
            throw new ValidationException("Project not found");
        }
        projectRepository.deleteById(id);
    }
}
