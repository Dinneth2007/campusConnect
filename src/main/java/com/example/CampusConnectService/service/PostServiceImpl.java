package com.example.CampusConnectService.service;

import com.example.CampusConnectService.dto.PostCreateDto;
import com.example.CampusConnectService.dto.PostResponseDto;
import com.example.CampusConnectService.entity.Post;
import com.example.CampusConnectService.entity.Tag;
import com.example.CampusConnectService.entity.User;
import com.example.CampusConnectService.mapper.PostMapper;
import com.example.CampusConnectService.repository.PostRepository;
import com.example.CampusConnectService.repository.TagRepository;
import com.example.CampusConnectService.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;
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
}