package com.example.demo.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.time.LocalDateTime;

@Entity
@Table(name = "images")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Image {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long userId;               // Ai upload ảnh

//    private String title;

//    @Column(columnDefinition = "TEXT")
//    private String description;

    private String url;

    private String status;        // draft / processing / published / private / deleted / blocked
    private String visibility;    // public / unlisted / private
    private boolean isPrivate;

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