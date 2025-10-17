package com.example.CampusConnectService.entity;

// Comment


import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "comments", indexes = {
        @Index(columnList = "post_id", name = "idx_comment_post")
})
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class Comment extends BaseEntity {


    @Column(length = 4000, nullable = false)
    private String content;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "author_id", nullable = false)
    private User author;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "post_id", nullable = false)
    private Post post;

    // for threaded replies
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_comment_id")
    private Comment parentComment;

    private boolean deleted = false;
}

