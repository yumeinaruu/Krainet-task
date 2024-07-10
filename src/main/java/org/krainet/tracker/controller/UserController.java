package org.krainet.tracker.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.krainet.tracker.exception.custom.CustomValidationException;
import org.krainet.tracker.model.User;
import org.krainet.tracker.model.dto.FindByNameDto;
import org.krainet.tracker.model.dto.user.UserCreateDto;
import org.krainet.tracker.model.dto.user.UserUpdateDto;
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
@Tag(name = "Work with users")
@SecurityRequirement(name = "Bearer Authentication")
@RequestMapping("/user")
public class UserController {
    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('USER' ,'ADMIN', 'SUPERADMIN')")
    @Operation(summary = "Gives info about all users")
    public ResponseEntity<List<User>> getAllUsers() {
        List<User> users = userService.getAllUsers();
        if (users.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(users, HttpStatus.OK);
    }

    @GetMapping("/id/{id}")
    @PreAuthorize("hasAnyRole('USER' ,'ADMIN', 'SUPERADMIN')")
    @Operation(summary = "Gives info about user by id")
    public ResponseEntity<User> getUserById(@PathVariable Long id) {
        Optional<User> user = userService.getUserById(id);
        if (user.isPresent()) {
            return new ResponseEntity<>(user.get(), HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @PostMapping("/name")
    @PreAuthorize("hasAnyRole('USER' ,'ADMIN', 'SUPERADMIN')")
    @Operation(summary = "Gives info about user by name")
    public ResponseEntity<User> getUserByName(@RequestBody @Valid FindByNameDto findByNameDto,
                                              BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            throw new CustomValidationException(bindingResult.getAllErrors().toString());
        }
        Optional<User> user = userService.getUserByName(findByNameDto.getName());
        if (user.isPresent()) {
            return new ResponseEntity<>(user.get(), HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @GetMapping("/name-sort")
    @PreAuthorize("hasAnyRole('USER' ,'ADMIN', 'SUPERADMIN')")
    @Operation(summary = "Gives info about all users sorted by name")
    public ResponseEntity<List<User>> getUsersSortedByName() {
        List<User> users = userService.getUsersSortedByName();
        if (users.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(users, HttpStatus.OK);
    }

    @GetMapping("/info")
    @PreAuthorize("hasAnyRole('USER' ,'ADMIN', 'SUPERADMIN')")
    @Operation(summary = "Gives info about current logged user")
    public ResponseEntity<User> getCurrentUser(Principal principal) {
        Optional<User> result = userService.getInfoAboutCurrentUser(principal.getName());
        if (result.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(result.get(), HttpStatus.OK);
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'SUPERADMIN')")
    @Operation(summary = "Create user")
    public ResponseEntity<HttpStatus> createUser(@RequestBody @Valid UserCreateDto userCreateDto,
                                                 BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            throw new CustomValidationException(bindingResult.getAllErrors().toString());
        }
        return new ResponseEntity<>(userService.createUser(userCreateDto) ? HttpStatus.CREATED : HttpStatus.BAD_REQUEST);
    }

    @PutMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'SUPERADMIN')")
    @Operation(summary = "Update user")
    public ResponseEntity<HttpStatus> updateUser(@RequestBody @Valid UserUpdateDto userUpdateDto,
                                                 BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            throw new CustomValidationException(bindingResult.getAllErrors().toString());
        }
        return new ResponseEntity<>(userService.updateUser(userUpdateDto) ? HttpStatus.NO_CONTENT : HttpStatus.BAD_REQUEST);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'SUPERADMIN')")
    @Operation(summary = "Delete user")
    public ResponseEntity<HttpStatus> deleteUser(@PathVariable Long id) {
        return new ResponseEntity<>(userService.deleteUserById(id) ? HttpStatus.NO_CONTENT : HttpStatus.BAD_REQUEST);
    }
}
