package com.example.demo.service;

import com.example.demo.model.PlaylistVideo;
import com.example.demo.model.UserPlaylist;
import com.example.demo.model.Video;
import com.example.demo.repository.PlayListVideoRepository;
import com.example.demo.repository.UserPlaylistRepository;
import com.example.demo.security.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserPlaylistService {
    private final UserPlaylistRepository userPlaylistRepository;
    private final PlayListVideoRepository playListVideoRepository;
    private final JwtService jwtService;
    private final UserService userService;

    public UserPlaylist createPlaylist(UserPlaylist userPlaylist) {
        return userPlaylistRepository.save(userPlaylist);
    }

    public List<UserPlaylist> findAllUserPlaylistByToken(String authHeader) {
        String token = jwtService.getTokenFromAuthHeader(authHeader);
        Long userId = userService.getUserIdByToken(token);
        return userPlaylistRepository.findByUserId(userId);
    }

    public PlaylistVideo addToPlaylist(Long videoId, Long playlistId, String authHeader) {
        String token = jwtService.getTokenFromAuthHeader(authHeader);
        Long userId = userService.getUserIdByToken(token);
        // Kiểm tra video đã tồn tại trong playlist chưa
        PlaylistVideo existed = playListVideoRepository.findByVideoIdAndPlaylistId(videoId, playlistId).get();
        if (existed != null) {
            throw new RuntimeException("Video already exists in this playlist");
        }
        PlaylistVideo playlistVideo = new PlaylistVideo();
        playlistVideo.setVideoId(videoId);
        playlistVideo.setPlaylistId(playlistId);
        return playListVideoRepository.save(playlistVideo);
    }

//    public PlaylistVideo addToPlaylist(Long videoId, Long playlistId, String authHeader) {
//        String token = jwtService.getTokenFromAuthHeader(authHeader);
//        Long userId = userService.getUserIdByToken(token);
//
//        // kiểm tra playlist có thuộc về user không
//        UserPlaylist playlist = userPlaylistRepository.findById(playlistId)
//                .orElseThrow(() -> new RuntimeException("Playlist not found"));
//
//        if (!playlist.getUserId().equals(userId)) {
//            throw new RuntimeException("You do not own this playlist");
//        }
//
//        // kiểm tra video đã có trong playlist chưa
//        PlaylistVideo existed = playListVideoRepository.findByVideoIdAndPlaylistId(videoId, playlistId);
//        if (existed != null) {
//            throw new RuntimeException("Video already exists in this playlist");
//        }
//
//        PlaylistVideo playlistVideo = new PlaylistVideo();
//        playlistVideo.setVideoId(videoId);
//        playlistVideo.setPlaylistId(playlistId);
//
//        return playListVideoRepository.save(playlistVideo);
//    }

    //xoa playlist, chinh sua playlist, them video vao playlist
}
