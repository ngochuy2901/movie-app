package com.example.demo.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "channel")
public class Channel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long userId;

    @Column(nullable = false)
    private String name;

    @Column(columnDefinition = "TEXT")
    private String description;

    private String avatarUrl;
    private String bannerUrl;


    private Long createdAt;

    @OneToOne(mappedBy = "channel", cascade = CascadeType.ALL)
    private ChannelStatistic statistic;


    @PrePersist
    public void onCreate() {
        this.createdAt = Instant.now().toEpochMilli();
    }
}