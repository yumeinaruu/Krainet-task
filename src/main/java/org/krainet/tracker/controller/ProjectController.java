package org.krainet.tracker.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.krainet.tracker.exception.custom.CustomValidationException;
import org.krainet.tracker.model.Project;
import org.krainet.tracker.model.dto.FindByNameDto;
import org.krainet.tracker.model.dto.project.ProjectCreateDto;
import org.krainet.tracker.model.dto.project.ProjectUpdateDescriptionDto;
import org.krainet.tracker.model.dto.project.ProjectUpdateDto;
import org.krainet.tracker.model.dto.project.ProjectUpdateNameDto;
import org.krainet.tracker.service.ProjectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;

@RestController
@Tag(name = "Work with projects")
@SecurityRequirement(name = "Bearer Authentication")
@RequestMapping("/project")
public class ProjectController {
    private final ProjectService projectService;

    @Autowired
    public ProjectController(ProjectService projectService) {
        this.projectService = projectService;
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('USER' ,'ADMIN', 'SUPERADMIN')")
    @Operation(summary = "Gives info about all projects")
    public ResponseEntity<List<Project>> getProjects() {
        List<Project> projects = projectService.getAllProjects();
        if (projects.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(projects, HttpStatus.OK);
    }

    @GetMapping("/id/{id}")
    @PreAuthorize("hasAnyRole('USER' ,'ADMIN', 'SUPERADMIN')")
    @Operation(summary = "Gives info about project by id")
    public ResponseEntity<Project> getProjectById(@PathVariable Long id) {
        Optional<Project> project = projectService.getProjectById(id);
        if (project.isPresent()) {
            return new ResponseEntity<>(project.get(), HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @PostMapping("/name")
    @PreAuthorize("hasAnyRole('USER' ,'ADMIN', 'SUPERADMIN')")
    @Operation(summary = "Gives info about project by name")
    public ResponseEntity<Project> getProjectByName(@RequestBody @Valid FindByNameDto findByNameDto,
                                                    BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            throw new CustomValidationException(bindingResult.getAllErrors().toString());
        }
        Optional<Project> project = projectService.getProjectByName(findByNameDto.getName());
        if (project.isPresent()) {
            return new ResponseEntity<>(project.get(), HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'SUPERADMIN')")
    @Operation(summary = "Create project")
    public ResponseEntity<HttpStatus> createProject(@RequestBody @Valid ProjectCreateDto projectCreateDto,
                                                    BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            throw new CustomValidationException(bindingResult.getAllErrors().toString());
        }
        return new ResponseEntity<>(projectService.createProject(projectCreateDto) ? HttpStatus.CREATED : HttpStatus.BAD_REQUEST);
    }

    @PutMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'SUPERADMIN')")
    @Operation(summary = "Update project")
    public ResponseEntity<HttpStatus> updateProject(@RequestBody @Valid ProjectUpdateDto projectUpdateDto,
                                                    BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            throw new CustomValidationException(bindingResult.getAllErrors().toString());
        }
        return new ResponseEntity<>(projectService.updateProject(projectUpdateDto) ? HttpStatus.NO_CONTENT : HttpStatus.BAD_REQUEST);
    }

    @PutMapping("/name")
    @PreAuthorize("hasAnyRole('ADMIN', 'SUPERADMIN')")
    @Operation(summary = "Update project's name")
    public ResponseEntity<HttpStatus> updateProjectName(@RequestBody @Valid ProjectUpdateNameDto projectUpdateNameDto,
                                                        BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            throw new CustomValidationException(bindingResult.getAllErrors().toString());
        }
        return new ResponseEntity<>(projectService.updateProjectName(projectUpdateNameDto) ? HttpStatus.NO_CONTENT : HttpStatus.BAD_REQUEST);
    }

    @PutMapping("/description")
    @PreAuthorize("hasAnyRole('ADMIN', 'SUPERADMIN')")
    @Operation(summary = "Update project's description")
    public ResponseEntity<HttpStatus> updateProjectDescription(@RequestBody @Valid ProjectUpdateDescriptionDto projectUpdateDescriptionDto,
                                                               BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            throw new CustomValidationException(bindingResult.getAllErrors().toString());
        }
        return new ResponseEntity<>(projectService.updateProjectDescription(projectUpdateDescriptionDto) ? HttpStatus.NO_CONTENT : HttpStatus.BAD_REQUEST);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'SUPERADMIN')")
    @Operation(summary = "Delete project")
    public ResponseEntity<HttpStatus> deleteProject(@PathVariable Long id) {
        return new ResponseEntity<>(projectService.deleteProject(id) ? HttpStatus.NO_CONTENT : HttpStatus.BAD_REQUEST);
    }
}
