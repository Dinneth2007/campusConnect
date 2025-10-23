package com.example.CampusConnectService.service;

import com.example.CampusConnectService.dto.Comment.CommentRequestDto;
import com.example.CampusConnectService.dto.Comment.CommentResponseDto;
import com.example.CampusConnectService.entity.Comment;
import com.example.CampusConnectService.entity.Post;
import com.example.CampusConnectService.entity.User;
import com.example.CampusConnectService.exception.ResourceNotFoundException;
import com.example.CampusConnectService.mapper.CommentMapper;
import com.example.CampusConnectService.repository.CommentRepository;
import com.example.CampusConnectService.repository.PostRepository;
import com.example.CampusConnectService.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CommentServiceImpl implements CommentService{

    private final CommentRepository commentRepository;
    private final CommentMapper mapper;
    private final UserRepository userRepository;
    private final PostRepository postRepository;

    @Override
    @Transactional
    public CommentResponseDto createComment(CommentRequestDto request) {
        User author = userRepository.findById(request.getAuthorId())
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + request.getAuthorId()));
        Post post = postRepository.findById(request.getPostId())
                .orElseThrow(() -> new ResourceNotFoundException("Post not found with id: " + request.getPostId()));

        Comment parent = null;
        if (request.getParentCommentId() != null) {
            parent = commentRepository.findById(request.getParentCommentId())
                    .orElseThrow(() -> new ResourceNotFoundException("Parent comment not found with id: " + request.getParentCommentId()));
        }

        Comment comment = Comment.builder()
                .content(request.getContent())
                .author(author)
                .post(post)
                .parentComment(parent)
                .deleted(false)
                .build();

        Comment saved = commentRepository.save(comment);
        return mapper.toDto(saved);
    }

    @Override
    @Transactional(readOnly = true)
    public CommentResponseDto getCommentById(Long id) {
        Comment comment = commentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Comment not found with id: " + id));
        if (comment.isDeleted()) {
            throw new ResourceNotFoundException("Comment not found with id: " + id);
        }
        return mapper.toDto(comment);
    }

    @Override
    @Transactional(readOnly = true)
    public List<CommentResponseDto> getCommentsByPostId(Long postId) {
        List<Comment> byPostIdAndDeletedFalse = commentRepository.findByPost_IdAndDeletedFalse(postId);
        return mapper.toCommentResponseDtoList(byPostIdAndDeletedFalse);

    }

    @Override
    @Transactional
    public CommentResponseDto updateComment(Long id, CommentRequestDto request) {
        Comment comment = commentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Comment not found with id: " + id));
        if (comment.isDeleted()) {
            throw new ResourceNotFoundException("Comment not found with id: " + id);
        }
        // Only allow updating content and parent relationship for simplicity
        comment.setContent(request.getContent());
        if (request.getParentCommentId() != null) {
            Comment parent = commentRepository.findById(request.getParentCommentId())
                    .orElseThrow(() -> new ResourceNotFoundException("Parent comment not found with id: " + request.getParentCommentId()));
            comment.setParentComment(parent);
        } else {
            comment.setParentComment(null);
        }

        Comment saved = commentRepository.save(comment);
        return mapper.toDto(saved);
    }

    @Override
    @Transactional
    public void deleteComment(Long id) {
        Comment comment = commentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Comment not found with id: " + id));
        if (!comment.isDeleted()) {
            comment.setDeleted(true);
            commentRepository.save(comment);
        }
    }
}
