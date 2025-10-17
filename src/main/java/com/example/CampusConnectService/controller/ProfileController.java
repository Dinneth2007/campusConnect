package com.example.CampusConnectService.controller;

import com.example.CampusConnectService.dto.ProfileDto;
import com.example.CampusConnectService.entity.StudentProfile;
import com.example.CampusConnectService.entity.User;
import com.example.CampusConnectService.repository.UserRepository;
import com.example.CampusConnectService.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/profile")
@RequiredArgsConstructor
public class ProfileController {

    private final UserRepository userRepository;
    private final UserService userService;

    @GetMapping("/me")
    public ResponseEntity<?> getMyProfile(@AuthenticationPrincipal String email) {
        User user = userRepository.findByEmail(email).orElseThrow();
        StudentProfile profile = user.getProfile();
        return ResponseEntity.ok(profile);
    }

    @PutMapping("/me")
    public ResponseEntity<StudentProfile> updateProfile(@AuthenticationPrincipal String email,
                                                        @Valid @RequestBody ProfileDto dto) {
        User user = userRepository.findByEmail(email).orElseThrow();
        StudentProfile updated = userService.updateProfile(user.getId(), dto);
        return ResponseEntity.ok(updated);
    }
}

