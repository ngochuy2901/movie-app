package com.example.demo.controller;

import com.example.demo.model.Comment;
import com.example.demo.model.Reaction;
import com.example.demo.service.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.parameters.P;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("comment")
public class CommentController {
    private final CommentService commentService;

    @GetMapping("{videoId}")
    public List<Comment> findAllCommentsByVideoId(@PathVariable Long videoId) {
        return commentService.findAllCommentsByVideoId(videoId);
    }

    @PostMapping("save_new_comment")
    public ResponseEntity<Comment> saveNewComment(@RequestParam String content, @RequestParam Long videoId, @RequestHeader("Authorization") String authHeader) {
        // authHeader = "Bearer <token>"
        String token = null;
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            token = authHeader.substring(7);
        }
        if (token == null) {
            return ResponseEntity.status(401).build(); // Unauthorized
        }
        return ResponseEntity.ok(commentService.saveNewComment(content, videoId, token));
    }
    @GetMapping("number_of_comments/{videoId}")
    public ResponseEntity<Long> countCommentByVideoId(@PathVariable Long videoId) {
        return ResponseEntity.ok(commentService.countCommentByVideoId(videoId));
    }

}
