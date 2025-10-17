package com.example.CampusConnectService.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class ProfileDto {
    @NotBlank
    private String displayName;

    private String major;

    private String university;

    private String bio;

    private String avatarUrl;
}