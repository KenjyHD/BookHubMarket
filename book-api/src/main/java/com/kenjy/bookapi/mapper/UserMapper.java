package com.kenjy.bookapi.mapper;

import com.kenjy.bookapi.entities.Users;
import com.kenjy.bookapi.dto.UserDto;

public interface UserMapper {

    UserDto toUserDto(Users user);
}