package com.example.CampusConnectService.controller;

import com.example.CampusConnectService.dto.PostCreateDto;
import com.example.CampusConnectService.dto.PostResponseDto;
import com.example.CampusConnectService.entity.Post;
import com.example.CampusConnectService.entity.User;
import com.example.CampusConnectService.repository.UserRepository;
import com.example.CampusConnectService.service.PostService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/post")
public class PostController {
    private final PostService postService;
    private final UserRepository userRepository;
    @PostMapping("/create")
    public ResponseEntity<PostResponseDto> create(@AuthenticationPrincipal String email,
                                                  @Valid @RequestBody PostCreateDto dto){
        User user = userRepository.findByEmail(email).orElseThrow();
        PostResponseDto post = postService.createPost(dto, user.getId());
        return ResponseEntity.ok().body(post);
    }

    @GetMapping("/find/")
    public ResponseEntity<Page<PostResponseDto>> getById(@AuthenticationPrincipal String email,

                                                         @RequestParam int page,
                                                         @RequestParam int size){
        User user = userRepository.findByEmail(email).orElseThrow();
        Page<PostResponseDto> postsByAuthor = postService.getPostsByAuthor(user.getId(), page, size);
        return ResponseEntity.ok().body(postsByAuthor);
    }
}
