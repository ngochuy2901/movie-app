package com.example.demo.repository;

import com.example.demo.model.PlaylistVideo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PlayListVideoRepository extends JpaRepository<PlaylistVideo, Long> {
    // Tìm 1 bản ghi theo videoId và playlistId
    Optional<PlaylistVideo> findByVideoIdAndPlaylistId(Long videoId, Long playlistId);

    // Nếu muốn kiểm tra tồn tại
    boolean existsByVideoIdAndPlaylistId(Long videoId, Long playlistId);
}
