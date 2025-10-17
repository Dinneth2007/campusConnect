package com.example.CampusConnectService.service;




import com.example.CampusConnectService.dto.ProfileDto;
import com.example.CampusConnectService.entity.StudentProfile;
import com.example.CampusConnectService.entity.User;
import com.example.CampusConnectService.repository.StudentProfileRepository;
import com.example.CampusConnectService.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final StudentProfileRepository profileRepository;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public User register(String email, String rawPassword) {
        if (userRepository.existsByEmail(email)) {
            throw new IllegalArgumentException("Email already registered");
        }

        User u = User.builder()
                .email(email)
                .passwordHash(passwordEncoder.encode(rawPassword))
                .enabled(true)
                .roles(Set.of("STUDENT"))
                .build();

        User saved = userRepository.save(u);

        // create empty profile for convenience (optional)
        StudentProfile profile = StudentProfile.builder()
                .user(saved)
                .displayName(email.split("@")[0]) // default display name
                .build();
        profileRepository.save(profile);
        saved.setProfile(profile);
        return saved;
    }

    public Optional<User> findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    @Transactional
    public StudentProfile updateProfile(Long userId, ProfileDto dto) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        StudentProfile profile = user.getProfile();
        if (profile == null) {
            profile = StudentProfile.builder().user(user).build();
        }

        profile.setDisplayName(dto.getDisplayName());
        profile.setMajor(dto.getMajor());
        profile.setUniversity(dto.getUniversity());
        profile.setBio(dto.getBio());
        profile.setAvatarUrl(dto.getAvatarUrl());

        return profileRepository.save(profile);
    }
}

