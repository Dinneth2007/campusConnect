package com.example.CampusConnectService.dto.Comment;

import jakarta.annotation.Nullable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CommentRequestDto {
    private String content;
    private Long authorId;
    private Long postId;

    private Long parentCommentId;
}

