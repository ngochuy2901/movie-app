package com.example.demo.service;

import com.example.demo.model.Comment;
import com.example.demo.model.User;
import com.example.demo.repository.CommentRepository;
import com.example.demo.repository.UserRepository;
import com.example.demo.security.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CommentService {
    private final CommentRepository commentRepository;
    private final UserRepository userRepository;
    private final JwtService jwtService;
    public List<Comment> findAllCommentsByVideoId(Long id) {
        return commentRepository.findByVideoIdOrderByCreatedAtDesc(id);
    }

    public long countCommentByVideoId(Long videoId) {
        return commentRepository.countByVideoId(videoId);
    }

    public Comment saveNewComment(String content, Long videoId, String token) {
        String username = jwtService.extractUsername(token);
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));
        Comment newComment = new Comment();
        newComment.setUserId(user.getId());
        newComment.setContent(content);
        newComment.setVideoId(videoId);
        return commentRepository.save(newComment);
    }

//    public Comment addComment(Comment comment) {
//        return commentRepository.save(comment);
//    }
}

