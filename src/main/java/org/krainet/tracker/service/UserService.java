package org.krainet.tracker.service;

import org.krainet.tracker.model.User;
import org.krainet.tracker.model.dto.user.UserCreateDto;
import org.krainet.tracker.model.dto.user.UserUpdateDto;
import org.krainet.tracker.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class UserService {
    private final UserRepository userRepository;

    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public Optional<User> getUserById(Long id) {
        return userRepository.findById(id);
    }

    public Optional<User> getUserByName(String name) {
        return userRepository.findByName(name);
    }

    //TODO::get info from security about current user

    public List<User> getUsersSortedByName() {
        return userRepository.findAll(Sort.by("name"));
    }

    public Boolean createUser(UserCreateDto userCreateDto) {
        User user = new User();
        user.setName(userCreateDto.getName());
        user.setCreated(Timestamp.valueOf(LocalDateTime.now()));
        user.setChanged(Timestamp.valueOf(LocalDateTime.now()));
        User savedUser = userRepository.save(user);
        return getUserById(savedUser.getId()).isPresent();
    }

    public Boolean updateUser(UserUpdateDto userUpdateDto) {
        Optional<User> userOptional = userRepository.findById(userUpdateDto.getId());
        if (userOptional.isPresent()) {
            User user = userOptional.get();
            user.setName(userUpdateDto.getName());
            user.setChanged(Timestamp.valueOf(LocalDateTime.now()));
            User savedUser = userRepository.saveAndFlush(user);
            return savedUser.equals(user);
        }
        return false;
    }

    public Boolean deleteUserById(Long id) {
        Optional<User> userOptional = userRepository.findById(id);
        if (userOptional.isEmpty()) {
            return false;
        }
        userRepository.delete(userOptional.get());
        return true;
    }
}
