package org.krainet.tracker.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.SequenceGenerator;
import lombok.Data;
import org.springframework.stereotype.Component;

@Entity(name = "projects")
@Component
@Data
public class Project {
    @Id
    @SequenceGenerator(name = "projectsIdSeqGen", sequenceName = "projects_id_seq", allocationSize = 1)
    @GeneratedValue(generator = "projectsIdSeqGen")
    private Long id;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "description")
    private String description;

    @JsonBackReference
    @JoinColumn(name = "user_id")
    @ManyToOne
    private User userId;
}
