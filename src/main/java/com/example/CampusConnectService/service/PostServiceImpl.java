package com.example.CampusConnectService.service;

import com.example.CampusConnectService.dto.PostCreateDto;
import com.example.CampusConnectService.dto.PostResponseDto;
import com.example.CampusConnectService.dto.PostUpdateDto;
import com.example.CampusConnectService.entity.Post;
import com.example.CampusConnectService.entity.Tag;
import com.example.CampusConnectService.entity.User;
import com.example.CampusConnectService.exception.ResourceNotFoundException;
import com.example.CampusConnectService.mapper.PostMapper;
import com.example.CampusConnectService.repository.PostRepository;
import com.example.CampusConnectService.repository.TagRepository;
import com.example.CampusConnectService.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.nio.file.AccessDeniedException;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PostServiceImpl implements PostService {
    private final PostRepository postRepository;
    private final TagRepository tagRepository;
    private final UserRepository userRepository;
    private final PostMapper mapper;

    @Override
    public PostResponseDto createPost(PostCreateDto dto, Long authorId) {
        User author = userRepository.findById(authorId)
                .orElseThrow(() -> new NoSuchElementException("User not found"));

        Set<Tag> tags = dto.getTags().stream()
                .map(this::findOrCreateTag)
                .collect(Collectors.toSet());

        Post post = Post.builder()
                .title(dto.getTitle())
                .content(dto.getContent())
                .author(author)
                .tags(tags)
                .build();

        Post save = postRepository.save(post);
        return mapper.toDto(save);
    }

    @Override
    public Page<PostResponseDto> getPostsByAuthor(Long authorId, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Post> postsPage = postRepository.findByAuthor_IdAndDeletedFalse(authorId, pageable);
        Page<PostResponseDto> map = postsPage.map(post -> mapper.toDto(post));
        return map;
    }



    // Helper: find existing tag by name or create and persist a new one
    private Tag findOrCreateTag(String name) {
        return tagRepository.findByName(name)
                .orElseGet(() -> tagRepository.save(Tag.builder().name(name).build()));
    }

    public Page<PostResponseDto> getAllPosts(Pageable pageable) {
        return postRepository.findByDeletedFalse(pageable).map(post -> mapper.toDto(post));
    }

    public void deletePost(Long postId, Long currentUserId) throws ResourceNotFoundException, AccessDeniedException {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new ResourceNotFoundException("Post not found"));

        if (!post.getAuthor().getId().equals(currentUserId)) {
            throw new AccessDeniedException("You cannot delete this post");
        }

        post.setDeleted(true);
        postRepository.save(post);
    }

    @Transactional
    public PostResponseDto updatePost(Long postId, Long userId, PostUpdateDto dto) throws ResourceNotFoundException, AccessDeniedException {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new ResourceNotFoundException("Post not found"));

        if (!post.getAuthor().getId().equals(userId)) {
            throw new AccessDeniedException("You cannot edit this post");
        }

        post.setTitle(dto.getTitle());
        post.setContent(dto.getContent());
        post.setTags(dto.getTags().stream()
                .map(this::findOrCreateTag)
                .collect(Collectors.toSet()));

       return  mapper.toDto( postRepository.save(post));
    }

    @Transactional
    public void softDeletePost(Long postId, Long userId) throws ResourceNotFoundException, AccessDeniedException {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new ResourceNotFoundException("Post not found"));

        if (!post.getAuthor().getId().equals(userId)) {
            throw new AccessDeniedException("You cannot delete this post");
        }

        post.setDeleted(true);
        postRepository.save(post);
    }


}