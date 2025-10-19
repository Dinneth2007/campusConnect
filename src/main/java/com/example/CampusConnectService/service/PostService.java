package com.example.CampusConnectService.service;

import com.example.CampusConnectService.dto.PostCreateDto;
import com.example.CampusConnectService.dto.PostResponseDto;
import com.example.CampusConnectService.dto.PostUpdateDto;
import com.example.CampusConnectService.exception.ResourceNotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.nio.file.AccessDeniedException;

public interface PostService {
    public PostResponseDto createPost(PostCreateDto postDto, Long authorId);
    public Page<PostResponseDto> getPostsByAuthor(Long authorId,int page, int size);
    public Page<PostResponseDto> getAllPosts(Pageable pageable);
    public void deletePost(Long postId, Long currentUserId) throws ResourceNotFoundException, AccessDeniedException;
    public PostResponseDto updatePost(Long postId, Long userId, PostUpdateDto dto) throws ResourceNotFoundException, AccessDeniedException;
    public void softDeletePost(Long postId, Long userId) throws ResourceNotFoundException, AccessDeniedException;
}
