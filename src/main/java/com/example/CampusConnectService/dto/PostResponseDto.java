package com.example.CampusConnectService.dto;

import com.example.CampusConnectService.entity.User;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.Instant;
import java.util.Set;

@Data
@AllArgsConstructor
public class PostResponseDto {
    private Long id;
    private String title;
    private String content;
    private User author;
    private Set<String> tags;
    private Instant createdAt;
}