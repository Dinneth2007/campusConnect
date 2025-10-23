package com.example.CampusConnectService.service;

import com.example.CampusConnectService.dto.Comment.CommentRequestDto;
import com.example.CampusConnectService.dto.Comment.CommentResponseDto;

import java.util.List;

public interface CommentService {
    CommentResponseDto createComment(CommentRequestDto request);
    CommentResponseDto getCommentById(Long id);
    List<CommentResponseDto> getCommentsByPostId(Long postId);
    CommentResponseDto updateComment(Long id, CommentRequestDto request);
    void deleteComment(Long id);
}
