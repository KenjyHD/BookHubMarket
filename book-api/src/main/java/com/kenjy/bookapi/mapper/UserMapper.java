package com.kenjy.bookapi.mapper;

import com.kenjy.bookapi.entities.User;
import com.kenjy.bookapi.dto.UserDto;

public interface UserMapper {

    UserDto toUserDto(User user);
}