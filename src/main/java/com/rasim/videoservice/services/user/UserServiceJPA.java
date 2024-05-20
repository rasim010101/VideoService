package com.rasim.videoservice.services.user;

import com.rasim.videoservice.dto.CommentDTO;
import com.rasim.videoservice.dto.UserDTO;
import com.rasim.videoservice.dto.authorization.AuthRegistrationDTO;
import com.rasim.videoservice.dto.authorization.CustomUserDetails;
import com.rasim.videoservice.entities.User;
import com.rasim.videoservice.entities.UserRole;
import com.rasim.videoservice.exceptions.NotFoundException;
import com.rasim.videoservice.mappers.UserMapper;
import com.rasim.videoservice.repositories.UserRepository;
import com.rasim.videoservice.repositories.UserRoleRepository;
import com.rasim.videoservice.services.comment.CommentService;
import com.rasim.videoservice.services.video.VideoService;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class UserServiceJPA implements UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final CommentService commentService;
    private final UserMapper userMapper;
    private final UserRoleRepository userRoleRepository;

    public UserServiceJPA(UserRepository userRepository, CommentService commentService, VideoService videoService, PasswordEncoder passwordEncoder, UserMapper userMapper, UserRoleRepository userRoleRepository) {
        this.userRepository = userRepository;
        this.commentService = commentService;
        this.passwordEncoder = passwordEncoder;
        this.userMapper = userMapper;
        this.userRoleRepository = userRoleRepository;
    }

    @Override
    public List<UserDTO> findAllUsers() {
        List<User> users = (List<User>) userRepository.findAll();
        return users.stream()
                .map(userMapper::userToUserDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<UserDTO> findUserByCommentedVideo(Long videoId) {
        List<CommentDTO> comments = commentService.findCommentsByVideoId(videoId);
        List<UserDTO> users = new ArrayList<>();
        for (CommentDTO commentDTO : comments) {
            Long userId = commentDTO.getUserId();
            Optional<User> userOptional = userRepository.findById(userId);
            userOptional.ifPresent(user -> users.add(userMapper.userToUserDto(user)));
        }
        return users;
    }

    @Override
    public Optional<UserDTO> findUserByID(Long id) {
        Optional<User> optionalUser = userRepository.findById(id);
        User user = optionalUser.orElseThrow(() -> new NotFoundException("User not found with id: " + id));
        return Optional.of(userMapper.userToUserDto(user));
    }

    @Override
    public UserDTO saveUser(UserDTO dto) {
        UserRole userRole = userRoleRepository.findByRoleName(UserRole.Name.USER)
                .orElseThrow(() -> new RuntimeException("USER role not found")); // Handle if the role doesn't exist

        User user = userMapper.userDtoToUser(dto);
        user.setRoles(Set.of(userRole));
        user.setPassword(passwordEncoder.encode(user.getUsername() + LocalDate.now().getYear()));
        User savedUser = userRepository.save(user);
        return userMapper.userToUserDto(savedUser);
    }

    @Override
    public UserDTO register(AuthRegistrationDTO authRegistrationDTO) {
        UserRole userRole = userRoleRepository.findByRoleName(UserRole.Name.USER)
                .orElseThrow(() -> new RuntimeException("USER role not found")); // Handle if the role doesn't exist
        User user = User.builder()
                .name(authRegistrationDTO.getName())
                .email(authRegistrationDTO.getEmail())
                .username(authRegistrationDTO.getUsername())
                .password(passwordEncoder.encode(authRegistrationDTO.getPassword()))
                .roles(Set.of(userRole))
                .build();
        User savedUser = userRepository.save(user);
        return userMapper.userToUserDto(savedUser);
    }

    @Override
    public void deleteUser(Long id) {
        if (!userRepository.existsById(id)) {
            throw new NotFoundException("User not found with id: " + id);
        }
        userRepository.deleteById(id);
    }

    public User getCurrentUser(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Object principal = authentication.getPrincipal();
        if (principal instanceof CustomUserDetails userDetails)
            return userDetails.user();
        else
            return null;
    }
}
