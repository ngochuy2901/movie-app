package com.example.demo.controller;

import com.example.demo.model.Reaction;
import com.example.demo.service.ReactionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("reaction")
@RequiredArgsConstructor
public class ReactionController {

    private final ReactionService reactionService;

    @PostMapping("on_reaction_click")
    public void onReactionClick(
            @RequestParam Long targetId,
            @RequestHeader("Authorization") String authHeader)
    {
        reactionService.onClickReaction(authHeader, targetId);
    }

    @GetMapping("count_reactions_by_target_id/{targetId}")
    public ResponseEntity<Long> countReactionByTargetId(@PathVariable("targetId") Long targetId) {
        return ResponseEntity.ok(reactionService.countReactionByTargetId(targetId));
    }
}
