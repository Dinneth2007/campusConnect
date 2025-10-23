package com.example.CampusConnectService.controller;

import com.example.CampusConnectService.dto.Post.PostCreateDto;
import com.example.CampusConnectService.dto.PostResponseDto;
import com.example.CampusConnectService.entity.User;
import com.example.CampusConnectService.exception.ResourceNotFoundException;
import com.example.CampusConnectService.repository.UserRepository;
import com.example.CampusConnectService.service.PostService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.nio.file.AccessDeniedException;

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

    @GetMapping
    public Page<PostResponseDto> getAllPosts(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        return postService.getAllPosts(PageRequest.of(page, size, Sort.by("createdAt").descending()));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePost(@PathVariable Long id, @AuthenticationPrincipal String email) throws AccessDeniedException, ResourceNotFoundException {
        User user = userRepository.findByEmail(email).orElseThrow();
        postService.softDeletePost(id, user.getId());
        return ResponseEntity.noContent().build();
    }
    


}
