package com.example.CampusConnectService.entity;

// User (auth)

import jakarta.persistence.*;
import lombok.*;
import java.util.Set;

@Entity

@Table(name = "users", indexes = {
        @Index(columnList = "email", name = "idx_user_email")
})

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class User extends BaseEntity {

    @Column(nullable = false, unique = true, length = 320)
    private String email;

    @Column(nullable = false)
    private String passwordHash;

    @Column(nullable = false)
    private boolean enabled = true;

    // roles stored as comma-separated or separate Role entity; simple example:
    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "user_roles", joinColumns = @JoinColumn(name = "user_id"))
    @Column(name = "role")
    private Set<String> roles;

    // One-to-one profile, cascade on persist but lazy fetch for performance
    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.LAZY, optional = true)
    private StudentProfile profile;
}


