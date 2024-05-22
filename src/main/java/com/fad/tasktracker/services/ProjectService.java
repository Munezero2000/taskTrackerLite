package com.fad.tasktracker.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fad.tasktracker.entity.Project;
import com.fad.tasktracker.repositories.ProjectRepository;
import com.fad.tasktracker.repositories.UserRepository;

import jakarta.validation.Valid;
import jakarta.validation.ValidationException;

import java.util.*;

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

    public Map<String, Object> updateProject(Project project) {
        Optional<Project> optionalProject = projectRepository.findById(project.getId());
        if (optionalProject.isEmpty()) {
            throw new ValidationException("Project not found");
        }

        Project existingProject = optionalProject.get();

        // Update the provided fields
        if (project.getName() != null) {
            existingProject.setName(project.getName());
        }
        if (project.getDescription() != null) {
            existingProject.setDescription(project.getDescription());
        }
        if (project.getExpectedEndDate() != null) {
            existingProject.setExpectedEndDate(project.getExpectedEndDate());
        }

        // Save the updated project
        Project savedProject = projectRepository.save(existingProject);

        // Prepare the response
        Map<String, Object> response = new HashMap<>();
        response.put("message", "Project updated successfully");
        response.put("project", savedProject);

        return response;
    }

    public void deleteProject(Long id) {
        if (!projectRepository.existsById(id)) {
            throw new ValidationException("Project not found");
        }
        projectRepository.deleteById(id);
    }
}
