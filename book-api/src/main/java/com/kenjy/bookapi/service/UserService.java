package com.kenjy.bookapi.service;

import com.kenjy.bookapi.dto.UpdateUserDTO;
import com.kenjy.bookapi.dto.UserDto;
import com.kenjy.bookapi.entities.User;
import com.kenjy.bookapi.exception.UserNotFoundException;
import com.kenjy.bookapi.mapper.UserMapper;
import com.kenjy.bookapi.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserMapper userMapper;

    public List<User> getUsers() {
        return userRepository.findAll();
    }

    public Optional<User> getUserByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    public boolean hasUserWithUsername(String username) {
        return userRepository.existsByUsername(username);
    }

    public boolean hasUserWithEmail(String email) {
        return userRepository.existsByEmail(email);
    }

    public User validateAndGetUserByUsername(String username) {
        return getUserByUsername(username)
                .orElseThrow(() -> new UserNotFoundException(String.format("Users with username %s not found", username)));
    }

    public User saveUser(User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return userRepository.save(user);
    }

    public UserDto deleteUser(String username) {
        User user = validateAndGetUserByUsername(username);
        userRepository.delete(user);
        return userMapper.toUserDto(user);
    }

    public Optional<User> validUsernameAndPassword(String username, String password) {
        return getUserByUsername(username)
                .filter(user -> passwordEncoder.matches(password, user.getPassword()));
    }

    public User updateUser(UpdateUserDTO dto) {
        String currentUsername = SecurityContextHolder.getContext().getAuthentication().getName();
        User currentUser = validateAndGetUserByUsername(currentUsername);

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

    public User validateAndGetUserById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException(String.format("User with id %d not found", id)));
    }

    public User findByUsername(String username) {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new UserNotFoundException(String.format("User with username {%s} not found", username)));
    }

    public User findUserById(Long userId) {
        return userRepository.findById(userId).orElseThrow(
                () -> new UserNotFoundException(String.format("User with id {%s} not found", userId)));
    }
}
