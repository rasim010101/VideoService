package com.rasim.videoservice.controllers;


import com.rasim.videoservice.dto.*;
import com.rasim.videoservice.dto.authorization.*;
import com.rasim.videoservice.entities.RefreshToken;
import com.rasim.videoservice.services.JwtService;
import com.rasim.videoservice.services.RefreshTokenService;
import com.rasim.videoservice.services.user.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
@Slf4j
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final RefreshTokenService refreshTokenService;
    private final UserService userService;


    @PostMapping("/register")
    public UserDTO register(@Validated @RequestBody AuthRegistrationDTO authRegistrationDTO){
        return userService.register(authRegistrationDTO);
    }

    @PostMapping("/login")
    public JwtTokenDto AuthenticateAndGetToken(@RequestBody AuthLoginDto authRequestDTO){
        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(authRequestDTO.getUsername(), authRequestDTO.getPassword()));
        if(authentication.isAuthenticated()){
            RefreshToken refreshToken = refreshTokenService.createRefreshToken(authRequestDTO.getUsername());
            return JwtTokenDto.builder()
                    .accessToken(jwtService.GenerateToken(authRequestDTO.getUsername()))
                    .token(refreshToken.getToken())
                    .build();

        } else {
            throw new UsernameNotFoundException("invalid user request..!!");
        }
    }
    @PostMapping("/refreshToken")
    public JwtTokenDto refreshToken(@RequestBody RefreshTokenRequestDTO refreshTokenRequestDTO){
        return refreshTokenService.findByToken(refreshTokenRequestDTO.getToken())
                .map(refreshTokenService::verifyExpiration)
                .map(RefreshToken::getUser)
                .map(userInfo -> {
                    String accessToken = jwtService.GenerateToken(userInfo.getUsername());
                    return JwtTokenDto.builder()
                            .accessToken(accessToken)
                            .token(refreshTokenRequestDTO.getToken()).build();
                }).orElseThrow(() ->new RuntimeException("Refresh Token is not in DB..!!"));
    }

    @GetMapping
    public Object getCurrentUser(){
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (principal instanceof CustomUserDetails userDetail) {
            return new UserDTO(userDetail.user());
        }
        return principal;
    }
}
