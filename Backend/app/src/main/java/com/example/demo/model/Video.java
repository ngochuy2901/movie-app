package com.example.demo.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;

@Entity
@Table(name = "videos")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Video {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long userId;
    private Long channelId;
    private Long PlaylistId;

    private String title;

    @Column(columnDefinition = "TEXT")
    private String description;

    private String url;
    private String urlThumbnail;

    private String status;        // draft / processing / published / private / deleted / blocked
    private String visibility;    // public / unlisted / private

    private Long publishedAt;     // timestamp
    private Long createdAt;       // timestamp
    private Long updatedAt;       // timestamp

    @PrePersist
    public void onCreate() {
        this.createdAt = Instant.now().toEpochMilli();
        this.updatedAt = Instant.now().toEpochMilli();
        if (this.status == null) this.status = "draft";
        if (this.visibility == null) this.visibility = "public";
    }

    @PreUpdate
    public void onUpdate() {
        this.updatedAt = Instant.now().toEpochMilli();
    }
}