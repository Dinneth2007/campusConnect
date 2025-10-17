package com.example.CampusConnectService.entity;

// Post


import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;
import java.util.Set;

@Entity
@Table(name = "posts", indexes = {
        @Index(columnList = "createdAt", name = "idx_post_created_at")
})
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class Post extends BaseEntity {

    @Column(nullable = false)
    private String title;

    @Column(length = 10000, nullable = false)
    private String content;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "author_id", nullable = false)
    private User author;

    // tags: unidirectional ManyToMany from Post -> Tag for simplicity
    @ManyToMany
    @JoinTable(name = "post_tags",
            joinColumns = @JoinColumn(name = "post_id"),
            inverseJoinColumns = @JoinColumn(name = "tag_id"))
    private Set<Tag> tags;

    // soft delete
    private boolean deleted = false;

    // optimistic locking
    @Version
    private Long version;
}
