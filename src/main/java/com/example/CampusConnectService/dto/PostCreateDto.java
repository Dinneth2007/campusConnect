package com.example.CampusConnectService.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.util.HashSet;
import java.util.Set;

@Data
public class PostCreateDto {
    @NotBlank
    private String title;
    @NotBlank
    private String content;
    private Set<String> tags = new HashSet<>();
}
