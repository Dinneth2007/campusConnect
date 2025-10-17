package com.example.CampusConnectService.controller;

import com.example.CampusConnectService.dto.AuthResponseDto;
import com.example.CampusConnectService.dto.LoginRequestDto;
import com.example.CampusConnectService.dto.UserRegistrationDto;
import com.example.CampusConnectService.entity.User;
import com.example.CampusConnectService.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("api/auth")

public class AuthController {
    private final AuthService authService;
    @PostMapping("/sign-up")
    public ResponseEntity<Void> sighnUp(@Valid @RequestBody UserRegistrationDto dto){
        User register = authService.register(dto);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponseDto> login(@Valid @RequestBody LoginRequestDto dto){
        try {
            String token = authService.login(dto);
            return ResponseEntity.ok().body(new AuthResponseDto(token));
        } catch (BadCredentialsException ex) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }
}
