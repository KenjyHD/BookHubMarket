package com.kenjy.bookapi.dto;

public record UserDto(Long id, String username, String name, String email, String role) {
}