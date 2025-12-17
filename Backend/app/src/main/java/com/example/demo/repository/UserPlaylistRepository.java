package com.example.demo.repository;

import com.example.demo.model.UserPlaylist;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserPlaylistRepository extends JpaRepository<UserPlaylist, Long> {
    List<UserPlaylist> findByUserId(Long userId);
}
