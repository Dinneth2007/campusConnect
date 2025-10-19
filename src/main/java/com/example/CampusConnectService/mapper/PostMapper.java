package com.example.CampusConnectService.mapper;

import com.example.CampusConnectService.dto.PostResponseDto;
import com.example.CampusConnectService.entity.Post;
import com.example.CampusConnectService.entity.Tag;
import org.mapstruct.Mapper;

import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring")
public interface PostMapper {
    PostResponseDto toDto(Post post);
    List<PostResponseDto> toDtoList(List<Post> posts);

    // MapStruct couldn't map Set<Tag> -> Set<String> automatically.
    // Provide a conversion method so the processor can compile the mapper.
    default Set<String> map(Set<Tag> tags) {
        if (tags == null) return Collections.emptySet();
        return tags.stream().map(Tag::getName).collect(Collectors.toSet());
    }
}
