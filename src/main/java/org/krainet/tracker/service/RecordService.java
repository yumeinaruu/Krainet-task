package org.krainet.tracker.service;

import org.krainet.tracker.exception.custom.NotThatUserUpdatesRecord;
import org.krainet.tracker.model.Project;
import org.krainet.tracker.model.Record;
import org.krainet.tracker.model.dto.record.RecordCreateDto;
import org.krainet.tracker.model.dto.record.RecordUpdateDeadlineDto;
import org.krainet.tracker.model.dto.record.RecordUpdateDto;
import org.krainet.tracker.model.dto.record.RecordUpdateProjectIdDto;
import org.krainet.tracker.model.dto.record.RecordUpdateStartedDto;
import org.krainet.tracker.model.dto.record.Status;
import org.krainet.tracker.repository.ProjectRepository;
import org.krainet.tracker.repository.RecordRepository;
import org.krainet.tracker.repository.UserRepository;
import org.krainet.tracker.security.model.Roles;
import org.krainet.tracker.security.model.Security;
import org.krainet.tracker.security.repository.SecurityRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class RecordService {
    private final RecordRepository recordRepository;
    private final ProjectRepository projectRepository;
    private final UserRepository userRepository;
    private final SecurityRepository securityRepository;

    @Autowired
    public RecordService(RecordRepository recordRepository, ProjectRepository projectRepository,
                         UserRepository userRepository, SecurityRepository securityRepository) {
        this.recordRepository = recordRepository;
        this.projectRepository = projectRepository;
        this.userRepository = userRepository;
        this.securityRepository = securityRepository;
    }

    public List<Record> getAllRecords() {
        return recordRepository.findAll();
    }

    public Optional<Record> getRecordById(Long id) {
        return recordRepository.findById(id);
    }

    public Boolean createRecord(RecordCreateDto recordCreateDto) {
        Record record = new Record();
        if (projectRepository.findByName(recordCreateDto.getProject().getName()).isPresent()) {
            record.setProjectId(projectRepository.findByName(recordCreateDto.getProject().getName()).get().getId());
        }
        if (userRepository.findByName(recordCreateDto.getUser().getName()).isPresent()) {
            record.setUserId(userRepository.findByName(recordCreateDto.getUser().getName()).get());
        }
        record.setStarted(recordCreateDto.getStarted());
        record.setStatus(Status.STARTED);
        Record savedRecord = recordRepository.save(record);
        return getRecordById(savedRecord.getId()).isPresent();
    }

    public Boolean updateRecord(RecordUpdateDto recordUpdateDto, String username) {
        Optional<Security> security = securityRepository.findByLogin(username);
        if (security.isEmpty()) {
            return false;
        }
        Optional<Record> recordOptional = recordRepository.findById(recordUpdateDto.getId());
        if (recordOptional.isPresent()) {
            Record record = recordOptional.get();
            record.setStarted(recordUpdateDto.getStarted());
            record.setDeadline(recordUpdateDto.getDeadline());
            if (projectRepository.findByName(recordUpdateDto.getProject().getName()).isPresent()) {
                record.setProjectId(projectRepository.findByName(recordUpdateDto.getProject().getName()).get().getId());
            }
            if (userRepository.findByName(recordUpdateDto.getUser().getName()).isPresent()) {
                record.setUserId(projectRepository.findByName(recordUpdateDto.getUser().getName()).get().getUserId());
            }
            if (!record.getUserId().equals(userRepository.findById(security.get().getUserId()).get()) &&
            security.get().getRole().equals(Roles.USER)) {
                throw new NotThatUserUpdatesRecord(username);
            }
            if (record.getDeadline().before(Timestamp.valueOf(LocalDateTime.now()))) {
                record.setStatus(Status.FINISHED);
            } else {
                record.setStatus(Status.STARTED);
            }
            Record savedRecord = recordRepository.saveAndFlush(record);
            return savedRecord.equals(record);
        }
        return false;
    }

    public Boolean updateRecordStarted(RecordUpdateStartedDto recordUpdateStartedDto, String username) {
        Optional<Security> security = securityRepository.findByLogin(username);
        if (security.isEmpty()) {
            return false;
        }
        Optional<Record> recordOptional = recordRepository.findById(recordUpdateStartedDto.getId());
        if (recordOptional.isPresent()) {
            Record record = recordOptional.get();
            if (!record.getUserId().equals(userRepository.findById(security.get().getUserId()).get()) &&
                    security.get().getRole().equals(Roles.USER)) {
                throw new NotThatUserUpdatesRecord(username);
            }
            record.setStarted(recordUpdateStartedDto.getStarted());
            Record savedRecord = recordRepository.saveAndFlush(record);
            return savedRecord.equals(record);
        }
        return false;
    }

    public Boolean updateRecordDeadline(RecordUpdateDeadlineDto recordUpdateDeadlineDto, String username) {
        Optional<Security> security = securityRepository.findByLogin(username);
        if (security.isEmpty()) {
            return false;
        }
        Optional<Record> recordOptional = recordRepository.findById(recordUpdateDeadlineDto.getId());
        if (recordOptional.isPresent()) {
            Record record = recordOptional.get();
            record.setDeadline(recordUpdateDeadlineDto.getDeadline());
            if (record.getDeadline().before(Timestamp.valueOf(LocalDateTime.now()))) {
                record.setStatus(Status.FINISHED);
            } else {
                record.setStatus(Status.STARTED);
            }
            if (!record.getUserId().equals(userRepository.findById(security.get().getUserId()).get()) &&
                    security.get().getRole().equals(Roles.USER)) {
                throw new NotThatUserUpdatesRecord(username);
            }
            Record savedRecord = recordRepository.saveAndFlush(record);
            return savedRecord.equals(record);
        }
        return false;
    }

    public Boolean updateRecordProjectId(RecordUpdateProjectIdDto recordUpdateProjectIdDto, String username) {
        Optional<Security> security = securityRepository.findByLogin(username);
        if (security.isEmpty()) {
            return false;
        }
        Optional<Record> recordOptional = recordRepository.findById(recordUpdateProjectIdDto.getId());
        if (recordOptional.isPresent()) {
            Record record = recordOptional.get();
            if (projectRepository.findByName(recordUpdateProjectIdDto.getProject().getName()).isPresent()) {
                record.setProjectId(projectRepository.findByName(recordUpdateProjectIdDto.getProject().getName()).get().getId());
            }
            if (!record.getUserId().equals(userRepository.findById(security.get().getUserId()).get()) &&
                    security.get().getRole().equals(Roles.USER)) {
                throw new NotThatUserUpdatesRecord(username);
            }
            Record savedRecord = recordRepository.saveAndFlush(record);
            return savedRecord.equals(record);
        }
        return false;
    }

    public Boolean projectCompleted(Long id, String username) {
        Optional<Security> security = securityRepository.findByLogin(username);
        if (security.isEmpty()) {
            return false;
        }
        Optional<Record> recordOptional = recordRepository.findById(id);
        if (recordOptional.isPresent()) {
            Record record = recordOptional.get();
            record.setDeadline(Timestamp.valueOf(LocalDateTime.now()));
            record.setStatus(Status.FINISHED);
            if (!record.getUserId().equals(userRepository.findById(security.get().getUserId()).get()) &&
                    security.get().getRole().equals(Roles.USER)) {
                throw new NotThatUserUpdatesRecord(username);
            }
            Record savedRecord = recordRepository.saveAndFlush(record);
            return savedRecord.equals(record);
        }
        return false;
    }

    public Boolean deleteRecordById(Long id) {
        Optional<Record> recordOptional = recordRepository.findById(id);
        if (recordOptional.isEmpty()) {
            return false;
        }
        recordRepository.delete(recordOptional.get());
        return true;
    }
}
