package com.kenjy.bookapi.service;

import com.kenjy.bookapi.dto.UpdateUserDTO;
import com.kenjy.bookapi.entities.Users;
import com.kenjy.bookapi.exception.UserNotFoundException;
import com.kenjy.bookapi.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public List<Users> getUsers() {
        return userRepository.findAll();
    }

    public Optional<Users> getUserByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    public boolean hasUserWithUsername(String username) {
        return userRepository.existsByUsername(username);
    }

    public boolean hasUserWithEmail(String email) {
        return userRepository.existsByEmail(email);
    }

    public Users validateAndGetUserByUsername(String username) {
        return getUserByUsername(username)
                .orElseThrow(() -> new UserNotFoundException(String.format("Users with username %s not found", username)));
    }

    public Users saveUser(Users user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return userRepository.save(user);
    }

    public void deleteUser(Users user) {
        userRepository.delete(user);
    }

    public Optional<Users> validUsernameAndPassword(String username, String password) {
        return getUserByUsername(username)
                .filter(user -> passwordEncoder.matches(password, user.getPassword()));
    }

    public Users updateUser(UpdateUserDTO dto) {
        String currentUsername = SecurityContextHolder.getContext().getAuthentication().getName();
        Users currentUser = validateAndGetUserByUsername(currentUsername);

        if (!currentUser.getEmail().equals(dto.getEmail()) && userRepository.existsByEmail(dto.getEmail())) {
            throw new RuntimeException("Email already in use");
        }

        currentUser.setName(dto.getName());
        currentUser.setEmail(dto.getEmail());
        if (dto.getPassword() != null && !dto.getPassword().isEmpty()) {
            currentUser.setPassword(passwordEncoder.encode(dto.getPassword()));
        }

        return userRepository.save(currentUser);
    }

    public Users validateAndGetUserById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException(String.format("User with id %d not found", id)));
    }

    public Users findByUsername(String username) {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new UserNotFoundException(String.format("User with username {%s} not found", username)));
    }
}
