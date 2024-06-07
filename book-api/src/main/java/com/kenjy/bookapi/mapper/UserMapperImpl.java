package com.kenjy.bookapi.mapper;

import com.kenjy.bookapi.dto.UpdateUserDTO;
import com.kenjy.bookapi.entities.User;
import com.kenjy.bookapi.dto.UserDto;
import org.springframework.stereotype.Service;

@Service
public class UserMapperImpl implements UserMapper {

    @Override
    public UserDto toUserDto(User user) {
        if (user == null) {
            return null;
        }
        return new UserDto(user.getId(), user.getUsername(), user.getName(), user.getEmail(), user.getRole());
    }

    @Override
    public User toUsers(UpdateUserDTO dto) {
        if (dto == null) {
            return null;
        }
        User user = new User();
        user.setName(dto.getName());
        user.setEmail(dto.getEmail());
        user.setRole(dto.getPassword());
        return user;
    }
}
