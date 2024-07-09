package org.krainet.tracker.service;

import org.krainet.tracker.model.Record;
import org.krainet.tracker.model.dto.record.RecordCreateDto;
import org.krainet.tracker.model.dto.record.RecordUpdateDeadlineDto;
import org.krainet.tracker.model.dto.record.RecordUpdateDto;
import org.krainet.tracker.model.dto.record.RecordUpdateProjectIdDto;
import org.krainet.tracker.model.dto.record.RecordUpdateStartedDto;
import org.krainet.tracker.repository.ProjectRepository;
import org.krainet.tracker.repository.RecordRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class RecordService {
    private final RecordRepository recordRepository;
    private final ProjectRepository projectRepository;

    @Autowired
    public RecordService(RecordRepository recordRepository, ProjectRepository projectRepository) {
        this.recordRepository = recordRepository;
        this.projectRepository = projectRepository;
    }

    public List<Record> getAllRecords() {
        return recordRepository.findAll();
    }

    public Optional<Record> getRecordById(Long id) {
        return recordRepository.findById(id);
    }

    public Boolean createRecord(RecordCreateDto recordCreateDto) {
        Record record = new Record();
        if(projectRepository.findByName(recordCreateDto.getProject().getName()).isPresent()){
            record.setProjectId(projectRepository.findByName(recordCreateDto.getProject().getName()).get().getId());
        }
        record.setStarted(recordCreateDto.getStarted());
        record.setDeadline(recordCreateDto.getDeadline());
        Record savedRecord = recordRepository.save(record);
        return getRecordById(savedRecord.getId()).isPresent();
    }

    public Boolean updateRecord(RecordUpdateDto recordUpdateDto) {
        Optional<Record> recordOptional = recordRepository.findById(recordUpdateDto.getId());
        if(recordOptional.isPresent()){
            Record record = recordOptional.get();
            record.setStarted(recordUpdateDto.getStarted());
            record.setDeadline(recordUpdateDto.getDeadline());
            if(projectRepository.findByName(recordUpdateDto.getProject().getName()).isPresent()){
                record.setProjectId(projectRepository.findByName(recordUpdateDto.getProject().getName()).get().getId());
            }
            Record savedRecord = recordRepository.saveAndFlush(record);
            return savedRecord.equals(record);
        }
        return false;
    }

    public Boolean updateRecordStarted(RecordUpdateStartedDto recordUpdateStartedDto) {
        Optional<Record> recordOptional = recordRepository.findById(recordUpdateStartedDto.getId());
        if(recordOptional.isPresent()){
            Record record = recordOptional.get();
            record.setStarted(recordUpdateStartedDto.getStarted());
            Record savedRecord = recordRepository.saveAndFlush(record);
            return savedRecord.equals(record);
        }
        return false;
    }

    public Boolean updateRecordDeadline(RecordUpdateDeadlineDto recordUpdateDeadlineDto) {
        Optional<Record> recordOptional = recordRepository.findById(recordUpdateDeadlineDto.getId());
        if(recordOptional.isPresent()){
            Record record = recordOptional.get();
            record.setDeadline(recordUpdateDeadlineDto.getDeadline());
            Record savedRecord = recordRepository.saveAndFlush(record);
            return savedRecord.equals(record);
        }
        return false;
    }

    public Boolean updateRecordProjectId(RecordUpdateProjectIdDto recordUpdateProjectIdDto) {
        Optional<Record> recordOptional = recordRepository.findById(recordUpdateProjectIdDto.getId());
        if(recordOptional.isPresent()){
            Record record = recordOptional.get();
            if(projectRepository.findByName(recordUpdateProjectIdDto.getProject().getName()).isPresent()){
                record.setProjectId(projectRepository.findByName(recordUpdateProjectIdDto.getProject().getName()).get().getId());
            }
            Record savedRecord = recordRepository.saveAndFlush(record);
            return savedRecord.equals(record);
        }
        return false;
    }

    public Boolean deleteRecordById(Long id) {
        Optional<Record> recordOptional = recordRepository.findById(id);
        if(recordOptional.isEmpty()){
            return false;
        }
        recordRepository.delete(recordOptional.get());
        return true;
    }
}
