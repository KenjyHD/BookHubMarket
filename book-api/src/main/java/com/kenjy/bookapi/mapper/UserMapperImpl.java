package com.kenjy.bookapi.mapper;

import com.kenjy.bookapi.dto.UpdateUserDTO;
import com.kenjy.bookapi.entities.Users;
import com.kenjy.bookapi.dto.UserDto;
import org.springframework.stereotype.Service;

@Service
public class UserMapperImpl implements UserMapper {

    @Override
    public UserDto toUserDto(Users user) {
        if (user == null) {
            return null;
        }
        return new UserDto(user.getId(), user.getUsername(), user.getName(), user.getEmail(), user.getRole());
    }

    @Override
    public Users toUsers(UpdateUserDTO dto) {
        if (dto == null) {
            return null;
        }
        Users user = new Users();
        user.setName(dto.getName());
        user.setEmail(dto.getEmail());
        user.setRole(dto.getPassword());
        return user;
    }
}
