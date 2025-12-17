package com.example.demo.service;

import com.example.demo.model.Reaction;
import com.example.demo.repository.ReactionRepository;
import com.example.demo.repository.UserRepository;
import com.example.demo.security.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ReactionService {
    private final ReactionRepository reactionRepository;
    private final UserService userService;
    private final JwtService jwtService;

    public void onClickReaction(String authHeader, Long targetId) {
        String token = jwtService.getTokenFromAuthHeader(authHeader);
        Long userId = userService.getUserIdByToken(token);

        Optional<Reaction> optionalReaction =
                reactionRepository.findByUserIdAndTargetId(userId, targetId);

        if (optionalReaction.isPresent()) {
            reactionRepository.delete(optionalReaction.get());
            return;
        }

        Reaction reaction = Reaction.builder()
                .userId(userId)
                .targetId(targetId)
                .build();

        reactionRepository.save(reaction);
    }

    public Long countReactionByTargetId(Long targetId) {
        return reactionRepository.countByTargetId(targetId);
    }


}
