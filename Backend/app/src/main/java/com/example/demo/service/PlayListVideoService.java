package com.example.demo.service;

import com.example.demo.model.PlaylistVideo;
import com.example.demo.repository.PlayListVideoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PlayListVideoService {
    private final PlayListVideoRepository playListVideoRepository;

    public PlaylistVideo addToPlaylist(PlaylistVideo playlistVideo) {
        return playListVideoRepository.save(playlistVideo);
    }

    public void deleteVideoFromPlaylistByVideoIdAndPlaylistId(Long videoId, Long playlistId) {
        PlaylistVideo playlistVideo = playListVideoRepository.findByVideoIdAndPlaylistId(videoId, playlistId).get();
        playListVideoRepository.delete(playlistVideo);
    }
}
