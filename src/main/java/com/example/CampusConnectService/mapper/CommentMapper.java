package com.example.CampusConnectService.mapper;

import com.example.CampusConnectService.dto.Comment.CommentResponseDto;
import com.example.CampusConnectService.entity.Comment;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface CommentMapper {
    @Mapping(source = "author.id", target = "authorId")
    @Mapping(source = "post.id", target = "postId")
    @Mapping(source = "parentComment.id", target = "parentCommentId")
    CommentResponseDto toDto(Comment comment);

    List<CommentResponseDto> toCommentResponseDtoList(List<Comment> comments);
}
