package org.krainet.tracker.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.krainet.tracker.exception.custom.CustomValidationException;
import org.krainet.tracker.model.Record;
import org.krainet.tracker.model.dto.record.RecordCreateDto;
import org.krainet.tracker.model.dto.record.RecordStartDto;
import org.krainet.tracker.model.dto.record.RecordUpdateEndedDto;
import org.krainet.tracker.model.dto.record.RecordUpdateDto;
import org.krainet.tracker.model.dto.record.RecordUpdateProjectIdDto;
import org.krainet.tracker.model.dto.record.RecordUpdateStartedDto;
import org.krainet.tracker.security.repository.SecurityRepository;
import org.krainet.tracker.service.RecordService;
import org.krainet.tracker.service.UserService;
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

import java.security.Principal;
import java.util.List;
import java.util.Optional;

@RestController
@Tag(name = "Work with records")
@SecurityRequirement(name = "Bearer Authentication")
@RequestMapping("/record")
public class RecordController {
    private final RecordService recordService;

    @Autowired
    public RecordController(RecordService recordService, UserService userService,
                            SecurityRepository securityRepository) {
        this.recordService = recordService;
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('USER' ,'ADMIN', 'SUPERADMIN')")
    @Operation(summary = "Gives info about all records")
    public ResponseEntity<List<Record>> getRecords() {
        List<Record> records = recordService.getAllRecords();
        if (records.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(records, HttpStatus.OK);
    }

    @GetMapping("/id/{id}")
    @PreAuthorize("hasAnyRole('USER' ,'ADMIN', 'SUPERADMIN')")
    @Operation(summary = "Gives info about record by id")
    public ResponseEntity<Record> getRecordById(@PathVariable Long id) {
        Optional<Record> record = recordService.getRecordById(id);
        if (record.isPresent()) {
            return new ResponseEntity<>(record.get(), HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('USER' ,'ADMIN', 'SUPERADMIN')")
    @Operation(summary = "Create record")
    public ResponseEntity<HttpStatus> createRecord(@RequestBody @Valid RecordCreateDto recordCreateDto,
                                                   BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            throw new CustomValidationException(bindingResult.getAllErrors().toString());
        }
        return new ResponseEntity<>(recordService.createRecord(recordCreateDto) ? HttpStatus.CREATED : HttpStatus.BAD_REQUEST);
    }

    @PostMapping("/start")
    @PreAuthorize("hasAnyRole('USER' ,'ADMIN', 'SUPERADMIN')")
    @Operation(summary = "Start record (Counts time right after you pressed execute")
    public ResponseEntity<HttpStatus> startRecord(@RequestBody @Valid RecordStartDto recordStartDto,
                                                  BindingResult bindingResult, Principal principal) {
        if (bindingResult.hasErrors()) {
            throw new CustomValidationException(bindingResult.getAllErrors().toString());
        }
        return new ResponseEntity<>(recordService.startRecord(recordStartDto, principal.getName()) ? HttpStatus.CREATED : HttpStatus.CONFLICT);
    }

    @PutMapping
    @PreAuthorize("hasAnyRole('USER' ,'ADMIN', 'SUPERADMIN')")
    @Operation(summary = "Update record")
    public ResponseEntity<HttpStatus> updateRecord(@RequestBody @Valid RecordUpdateDto recordUpdateDto,
                                                   BindingResult bindingResult, Principal principal) {
        if (bindingResult.hasErrors()) {
            throw new CustomValidationException(bindingResult.getAllErrors().toString());
        }
        return new ResponseEntity<>(recordService.updateRecord(recordUpdateDto, principal.getName()) ? HttpStatus.NO_CONTENT : HttpStatus.BAD_REQUEST);
    }

    @PutMapping("/started")
    @PreAuthorize("hasAnyRole('USER' ,'ADMIN', 'SUPERADMIN')")
    @Operation(summary = "Update record's started time")
    public ResponseEntity<HttpStatus> updateRecordStarted(@RequestBody @Valid RecordUpdateStartedDto recordUpdateStartedDto,
                                                          BindingResult bindingResult, Principal principal) {
        if (bindingResult.hasErrors()) {
            throw new CustomValidationException(bindingResult.getAllErrors().toString());
        }
        return new ResponseEntity<>(recordService.updateRecordStarted(recordUpdateStartedDto, principal.getName()) ? HttpStatus.NO_CONTENT : HttpStatus.BAD_REQUEST);
    }

    @PutMapping("/ended")
    @PreAuthorize("hasAnyRole('USER' ,'ADMIN', 'SUPERADMIN')")
    @Operation(summary = "Update record's ending time")
    public ResponseEntity<HttpStatus> updateRecordEnded(@RequestBody @Valid RecordUpdateEndedDto recordUpdateDeadlineDto,
                                                           BindingResult bindingResult, Principal principal) {
        if (bindingResult.hasErrors()) {
            throw new CustomValidationException(bindingResult.getAllErrors().toString());
        }
        return new ResponseEntity<>(recordService.updateRecordEnded(recordUpdateDeadlineDto, principal.getName()) ? HttpStatus.NO_CONTENT : HttpStatus.BAD_REQUEST);
    }

    @PutMapping("/project")
    @PreAuthorize("hasAnyRole('ADMIN', 'SUPERADMIN')")
    @Operation(summary = "Update record's project")
    public ResponseEntity<HttpStatus> updateRecordProject(@RequestBody @Valid RecordUpdateProjectIdDto recordUpdateProjectIdDto,
                                                          BindingResult bindingResult, Principal principal) {
        if (bindingResult.hasErrors()) {
            throw new CustomValidationException(bindingResult.getAllErrors().toString());
        }
        return new ResponseEntity<>(recordService.updateRecordProjectId(recordUpdateProjectIdDto, principal.getName()) ? HttpStatus.NO_CONTENT : HttpStatus.BAD_REQUEST);
    }

    @PostMapping("/end/{id}")
    @PreAuthorize("hasAnyRole('USER' ,'ADMIN', 'SUPERADMIN')")
    @Operation(summary = "End record")
    public ResponseEntity<HttpStatus> makeRecordEnd(@PathVariable Long id, Principal principal) {
        return new ResponseEntity<>(recordService.projectCompleted(id, principal.getName()) ? HttpStatus.NO_CONTENT : HttpStatus.CONFLICT);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'SUPERADMIN')")
    @Operation(summary = "Delete record")
    public ResponseEntity<HttpStatus> deleteRecord(@PathVariable Long id) {
        return new ResponseEntity<>(recordService.deleteRecordById(id) ? HttpStatus.NO_CONTENT : HttpStatus.BAD_REQUEST);
    }
}
