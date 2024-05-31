package com.kenjy.bookapi.service;

import com.kenjy.bookapi.dto.UpdateUserDTO;
import com.kenjy.bookapi.entities.Users;

import java.util.List;
import java.util.Optional;

public interface UserService {
    List<Users> getUsers();
    Optional<Users> getUserByUsername(String username);
    boolean hasUserWithUsername(String username);
    boolean hasUserWithEmail(String email);
    Users validateAndGetUserByUsername(String username);
    Users saveUser(Users user);
    void deleteUser(Users user);
    Optional<Users> validUsernameAndPassword(String username, String password);
    Users updateUser(UpdateUserDTO dto);
    Users validateAndGetUserById(Long id);
}
