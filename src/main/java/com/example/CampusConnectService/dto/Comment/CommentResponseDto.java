package com.example.CampusConnectService.dto.Comment;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CommentResponseDto {

    private Long id;

    private String content;

    private Long authorId;

    private Long postId;

    private Long parentCommentId;

    private Instant createdAt;

    private Instant updatedAt;
}
