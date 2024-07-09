package org.krainet.tracker.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import lombok.Data;
import org.krainet.tracker.model.dto.record.Status;
import org.springframework.stereotype.Component;

import java.sql.Timestamp;

@Entity(name = "records")
@Component
@Data
public class Record {
    @Id
    @SequenceGenerator(name = "recordsIdSeqGen", sequenceName = "records_id_seq", allocationSize = 1)
    @GeneratedValue(generator = "recordsIdSeqGen")
    private Long id;

    @Column(name = "started", nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Timestamp started;

    @Column(name = "deadline")
    @Temporal(TemporalType.TIMESTAMP)
    private Timestamp deadline;

    @Column(name = "project_id", nullable = false)
    private Long projectId;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private Status status;
}
