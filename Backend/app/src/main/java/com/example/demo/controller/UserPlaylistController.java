package com.example.demo.controller;

import com.example.demo.model.PlaylistVideo;
import com.example.demo.model.UserPlaylist;
import com.example.demo.model.Video;
import com.example.demo.service.PlayListVideoService;
import com.example.demo.service.UserPlaylistService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.parameters.P;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("user_playlist")
public class UserPlaylistController {
    private final UserPlaylistService userPlaylistService;
    private final PlayListVideoService playListVideoService;
    @GetMapping("get_user_playlists")
    public ResponseEntity<List<UserPlaylist>> getUserPlaylists(@RequestHeader("Authorization") String authHeader) {
        return ResponseEntity.ok(userPlaylistService.findAllUserPlaylistByToken(authHeader));
    }

    @PostMapping("create_user_playlist")
    public ResponseEntity<UserPlaylist> createUserPlaylist(@RequestBody UserPlaylist userPlaylist) {
        return ResponseEntity.ok(userPlaylist);
    }

    @PostMapping("add_video_to_playlist")
    public ResponseEntity<?> addVideoToPlaylist(@RequestParam("videoId") Long videoId, @RequestParam("playlistId")Long playlistId) {
        PlaylistVideo playlistVideo = new PlaylistVideo();
        playlistVideo.setPlaylistId(playlistId);
        playlistVideo.setVideoId(videoId);
        return ResponseEntity.ok(playListVideoService.addToPlaylist(playlistVideo));
    }
}
