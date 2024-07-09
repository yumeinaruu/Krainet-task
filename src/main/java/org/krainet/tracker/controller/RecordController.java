package org.krainet.tracker.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.krainet.tracker.exception.custom.CustomValidationException;
import org.krainet.tracker.model.Record;
import org.krainet.tracker.model.dto.record.RecordCreateDto;
import org.krainet.tracker.model.dto.record.RecordUpdateDeadlineDto;
import org.krainet.tracker.model.dto.record.RecordUpdateDto;
import org.krainet.tracker.model.dto.record.RecordUpdateProjectIdDto;
import org.krainet.tracker.model.dto.record.RecordUpdateStartedDto;
import org.krainet.tracker.service.RecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
@Tag(name = "Work with records")
@RequestMapping("/record")
public class RecordController {
    private final RecordService recordService;

    @Autowired
    public RecordController(RecordService recordService) {
        this.recordService = recordService;
    }

    @GetMapping
    public ResponseEntity<List<Record>> getRecords() {
        List<Record> records = recordService.getAllRecords();
        if (records.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(records, HttpStatus.OK);
    }

    @GetMapping("/id/{id}")
    public ResponseEntity<Record> getRecordById(@PathVariable Long id) {
        Optional<Record> record = recordService.getRecordById(id);
        if (record.isPresent()) {
            return new ResponseEntity<>(record.get(), HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @PostMapping
    public ResponseEntity<HttpStatus> createRecord(@RequestBody @Valid RecordCreateDto recordCreateDto,
                                                   BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            throw new CustomValidationException(bindingResult.getAllErrors().toString());
        }
        return new ResponseEntity<>(recordService.createRecord(recordCreateDto) ? HttpStatus.CREATED : HttpStatus.BAD_REQUEST);
    }

    @PutMapping
    public ResponseEntity<HttpStatus> updateRecord(@RequestBody @Valid RecordUpdateDto recordUpdateDto,
                                                   BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            throw new CustomValidationException(bindingResult.getAllErrors().toString());
        }
        return new ResponseEntity<>(recordService.updateRecord(recordUpdateDto) ? HttpStatus.NO_CONTENT : HttpStatus.BAD_REQUEST);
    }

    @PutMapping("/started")
    public ResponseEntity<HttpStatus> updateRecordStarted(@RequestBody @Valid RecordUpdateStartedDto recordUpdateStartedDto,
                                                          BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            throw new CustomValidationException(bindingResult.getAllErrors().toString());
        }
        return new ResponseEntity<>(recordService.updateRecordStarted(recordUpdateStartedDto) ? HttpStatus.NO_CONTENT : HttpStatus.BAD_REQUEST);
    }

    @PutMapping("/deadline")
    public ResponseEntity<HttpStatus> updateRecordDeadline(@RequestBody @Valid RecordUpdateDeadlineDto recordUpdateDeadlineDto,
                                                           BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            throw new CustomValidationException(bindingResult.getAllErrors().toString());
        }
        return new ResponseEntity<>(recordService.updateRecordDeadline(recordUpdateDeadlineDto) ? HttpStatus.NO_CONTENT : HttpStatus.BAD_REQUEST);
    }

    @PutMapping("/project")
    public ResponseEntity<HttpStatus> updateRecordProject(@RequestBody @Valid RecordUpdateProjectIdDto recordUpdateProjectIdDto,
                                                          BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            throw new CustomValidationException(bindingResult.getAllErrors().toString());
        }
        return new ResponseEntity<>(recordService.updateRecordProjectId(recordUpdateProjectIdDto) ? HttpStatus.NO_CONTENT : HttpStatus.BAD_REQUEST);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<HttpStatus> deleteRecord(@PathVariable Long id) {
        return new ResponseEntity<>(recordService.deleteRecordById(id) ? HttpStatus.NO_CONTENT : HttpStatus.BAD_REQUEST);
    }
}
