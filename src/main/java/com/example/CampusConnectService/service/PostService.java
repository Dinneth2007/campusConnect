package com.example.CampusConnectService.service;

import com.example.CampusConnectService.dto.PostCreateDto;
import com.example.CampusConnectService.dto.PostResponseDto;
import org.springframework.data.domain.Page;

public interface PostService {
    public PostResponseDto createPost(PostCreateDto postDto, Long authorId);
    public Page<PostResponseDto> getPostsByAuthor(Long authorId,int page, int size);
}
