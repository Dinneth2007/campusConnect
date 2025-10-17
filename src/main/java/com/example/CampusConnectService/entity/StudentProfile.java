package com.example.CampusConnectService.entity;

// StudentProfile


import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "student_profiles", indexes = {
        @Index(columnList = "major", name = "idx_profile_major"),
        @Index(columnList = "university", name = "idx_profile_university")
})
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class StudentProfile extends BaseEntity {

    @OneToOne
    @JoinColumn(name = "user_id", nullable = false, unique = true)
    private User user;

    @Column(nullable = false)
    private String displayName;

    private String major;
    private String university;
    @Column(length = 2000)
    private String bio;

    private String avatarUrl;

    // version for optimistic locking
    @Version
    private Long version;
}

