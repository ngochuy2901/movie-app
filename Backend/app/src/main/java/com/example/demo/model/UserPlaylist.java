package com.example.demo.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "user_playlist")
public class UserPlaylist {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long userId;
    private String title;

    @Column(columnDefinition = "TEXT")
    private String description;

    private String privacy; // PUBLIC, PRIVATE, UNLISTED

    private Long createdAt;

    @PrePersist
    public void onCreate() {
        this.createdAt = Instant.now().toEpochMilli();
    }
}