package com.example.CampusConnectService.dto.Post;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.util.HashSet;
import java.util.Set;

@Data
public class PostUpdateDto {
    @NotBlank
    private String title;
    @NotBlank
    private String content;
    private Set<String> tags = new HashSet<>();
}
