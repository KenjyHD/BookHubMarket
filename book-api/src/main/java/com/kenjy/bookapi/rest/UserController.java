package com.kenjy.bookapi.rest;

import com.kenjy.bookapi.dto.UpdateUserDTO;
import com.kenjy.bookapi.mapper.UserMapper;
import com.kenjy.bookapi.entities.Users;
import com.kenjy.bookapi.dto.UserDto;
import com.kenjy.bookapi.security.CustomUserDetails;
import com.kenjy.bookapi.service.UserService;
import com.kenjy.bookapi.config.SwaggerConfig;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;
    private final UserMapper userMapper;

    @Operation(security = {@SecurityRequirement(name = SwaggerConfig.BASIC_AUTH_SECURITY_SCHEME)})
    @GetMapping("/me")
    public UserDto getCurrentUser(@AuthenticationPrincipal CustomUserDetails currentUser) {
        return userMapper.toUserDto(userService.validateAndGetUserByUsername(currentUser.getUsername()));
    }

    @Operation(security = {@SecurityRequirement(name = SwaggerConfig.BASIC_AUTH_SECURITY_SCHEME)})
    @GetMapping
    public List<UserDto> getUsers() {
        return userService.getUsers().stream()
                .map(userMapper::toUserDto)
                .collect(Collectors.toList());
    }

    @Operation(security = {@SecurityRequirement(name = SwaggerConfig.BASIC_AUTH_SECURITY_SCHEME)})
    @GetMapping("/search/{username}")
    public UserDto getUser(@PathVariable String username) {
        return userMapper.toUserDto(userService.validateAndGetUserByUsername(username));
    }

    @Operation(security = {@SecurityRequirement(name = SwaggerConfig.BASIC_AUTH_SECURITY_SCHEME)})
    @DeleteMapping("/{username}")
    public UserDto deleteUser(@PathVariable String username) {
        Users user = userService.validateAndGetUserByUsername(username);
        userService.deleteUser(user);
        return userMapper.toUserDto(user);
    }

    @Operation(security = {@SecurityRequirement(name = SwaggerConfig.BASIC_AUTH_SECURITY_SCHEME)})
    @PutMapping("/update")
    public ResponseEntity<Users> updateUser(@RequestBody UpdateUserDTO dto) {
        Users updatedUser = userService.updateUser(dto);
        return ResponseEntity.ok(updatedUser);
    }

    @Operation(security = {@SecurityRequirement(name = SwaggerConfig.BASIC_AUTH_SECURITY_SCHEME)})
    @GetMapping("/{id}")
    public UserDto getUserById(@PathVariable Long id) {
        Users user = userService.validateAndGetUserById(id);
        return userMapper.toUserDto(user);
    }
}
