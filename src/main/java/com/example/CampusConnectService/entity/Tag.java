package com.example.CampusConnectService.entity;

// Tag


import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "tags", uniqueConstraints = @UniqueConstraint(columnNames = "name"))
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class Tag extends BaseEntity {
    @Column(nullable = false, length = 100)
    private String name;
}
