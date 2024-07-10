package org.krainet.tracker.service;

import org.krainet.tracker.model.Project;
import org.krainet.tracker.model.dto.project.ProjectCreateDto;
import org.krainet.tracker.model.dto.project.ProjectUpdateDescriptionDto;
import org.krainet.tracker.model.dto.project.ProjectUpdateDto;
import org.krainet.tracker.model.dto.project.ProjectUpdateNameDto;
import org.krainet.tracker.repository.ProjectRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class ProjectService {
    private final ProjectRepository projectRepository;

    @Autowired
    public ProjectService(ProjectRepository projectRepository) {
        this.projectRepository = projectRepository;
    }

    public List<Project> getAllProjects() {
        return projectRepository.findAll();
    }

    public Optional<Project> getProjectById(Long id) {
        return projectRepository.findById(id);
    }

    public Optional<Project> getProjectByName(String name) {
        return projectRepository.findByName(name);
    }

    public Boolean createProject(ProjectCreateDto projectCreateDto) {
        Project project = new Project();
        project.setName(projectCreateDto.getName());
        project.setDescription(projectCreateDto.getDescription());
        project.setCreated(Timestamp.valueOf(LocalDateTime.now()));
        project.setChanged(Timestamp.valueOf(LocalDateTime.now()));
        Project savedProject = projectRepository.save(project);
        return getProjectById(savedProject.getId()).isPresent();
    }

    public Boolean updateProject(ProjectUpdateDto projectUpdateDto) {
        Optional<Project> projectOptional = getProjectById(projectUpdateDto.getId());
        if (projectOptional.isPresent()) {
            Project project = projectOptional.get();
            project.setName(projectUpdateDto.getName());
            project.setDescription(projectUpdateDto.getDescription());
            project.setChanged(Timestamp.valueOf(LocalDateTime.now()));
            Project savedProject = projectRepository.saveAndFlush(project);
            return savedProject.equals(project);
        }
        return false;
    }

    public Boolean updateProjectName(ProjectUpdateNameDto projectUpdateNameDto) {
        Optional<Project> projectOptional = getProjectById(projectUpdateNameDto.getId());
        if (projectOptional.isPresent()) {
            Project project = projectOptional.get();
            project.setName(projectUpdateNameDto.getName());
            project.setChanged(Timestamp.valueOf(LocalDateTime.now()));
            Project savedProject = projectRepository.saveAndFlush(project);
            return savedProject.equals(project);
        }
        return false;
    }

    public Boolean updateProjectDescription(ProjectUpdateDescriptionDto projectUpdateDescriptionDto) {
        Optional<Project> projectOptional = getProjectById(projectUpdateDescriptionDto.getId());
        if (projectOptional.isPresent()) {
            Project project = projectOptional.get();
            project.setDescription(projectUpdateDescriptionDto.getDescription());
            project.setChanged(Timestamp.valueOf(LocalDateTime.now()));
            Project savedProject = projectRepository.saveAndFlush(project);
            return savedProject.equals(project);
        }
        return false;
    }

    public Boolean deleteProject(Long id) {
        Optional<Project> projectOptional = getProjectById(id);
        if (projectOptional.isEmpty()) {
            return false;
        }
        projectRepository.delete(projectOptional.get());
        return true;
    }
}
