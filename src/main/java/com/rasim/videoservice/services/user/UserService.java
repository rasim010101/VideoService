package com.rasim.videoservice.services.user;

import com.rasim.videoservice.dto.UserDTO;
import com.rasim.videoservice.dto.authorization.AuthRegistrationDTO;

import java.util.List;
import java.util.Optional;

public interface UserService {

    List<UserDTO> findAllUsers();

    List<UserDTO> findUserByCommentedVideo(Long id);

    Optional<UserDTO> findUserByID(Long id);

    UserDTO saveUser(UserDTO dto);
    UserDTO register(AuthRegistrationDTO dto);

    void deleteUser(Long id);
}
