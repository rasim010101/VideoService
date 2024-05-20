package com.rasim.videoservice.controllers;

import com.rasim.videoservice.dto.UserDTO;
import com.rasim.videoservice.entities.User;
import com.rasim.videoservice.entities.UserRole;
import com.rasim.videoservice.exceptions.NotFoundException;
import com.rasim.videoservice.services.user.UserService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Set;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/admin")
@Log4j2
public class UserApiController {

    private final String COMMENTED_PATH = "/commented";
    private final String USER_PATH = "/user";
    private final String ID_PATH = USER_PATH + "/{id}";

    private final UserService userService;

    @GetMapping(USER_PATH)
    public List<UserDTO> getAllUsers() {
        return userService.findAllUsers();
    }

    @GetMapping(USER_PATH + COMMENTED_PATH + ID_PATH)
    public List<UserDTO> getUsersByComment(@PathVariable Long id) {
        log.info("Getting users with using like, userId: {}", id);
        return userService.findUserByCommentedVideo(id);
    }
    @GetMapping(ID_PATH)
    public UserDTO getById(@PathVariable Long id) {
        log.info("Getting user with id: {}", id);
        return userService.findUserByID(id).orElseThrow(() -> new NotFoundException("User not found with id: " + id));
    }

    @PostMapping(USER_PATH)
    public UserDTO createUser(@Validated @RequestBody UserDTO userDTO) {
        return userService.saveUser(userDTO);
    }

    @PutMapping(ID_PATH)
    public UserDTO updateUser(@PathVariable Long id, @Validated @RequestBody UserDTO userDTO) {
        userService.findUserByID(id).orElseThrow(() -> new NotFoundException("User not found with id: " + id));
        userDTO.setId(id);
        return userService.saveUser(userDTO);
    }

    @Transactional
    @PatchMapping(ID_PATH)
    public UserDTO updateUser(@PathVariable Long id, @RequestBody Map<String, String> updates) {
        UserDTO user = userService.findUserByID(id)
                .orElseThrow(() -> new NotFoundException("User not found with id: " + id));

        updates.forEach((key, value) -> {
            switch (key) {
                case "name":
                    user.setName(value);
                    break;
                case "username":
                    user.setUsername(value);
                    break;
                case "email":
                    user.setEmail(value);
                    break;
                default:
                    break;
            }
        });

        userService.saveUser(user);

        return user;
    }

    @DeleteMapping(ID_PATH)
    public void deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
    }

    public boolean isAdmin(User user) {
        // Get the set of roles associated with the user
        Set<UserRole> roles = user.getRoles();

        // Check if the roles set contains the ADMIN role
        for (UserRole role : roles) {
            if (role.getRoleName() == UserRole.Name.ADMIN) {
                return true; // User has the ADMIN role
            }
        }

        return false; // User does not have the ADMIN role
    }
}

