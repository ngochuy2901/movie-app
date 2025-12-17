package com.example.demo.model;

import jakarta.persistence.*;
import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "channel_statistic")
public class ChannelStatistic {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private long subscriberCount;
    private long totalViews;
    private long videoCount;

    @OneToOne
    @JoinColumn(name = "channel_id", referencedColumnName = "id")
    private Channel channel;
}