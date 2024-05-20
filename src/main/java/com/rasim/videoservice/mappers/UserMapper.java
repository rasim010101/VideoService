package com.rasim.videoservice.mappers;

import com.rasim.videoservice.dto.UserDTO;
import com.rasim.videoservice.entities.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper
public interface UserMapper {

    @Mapping(target = "password", ignore = true) // Ignore password field in mapping
    User userDtoToUser(UserDTO dto);

    UserDTO userToUserDto(User user);
}
