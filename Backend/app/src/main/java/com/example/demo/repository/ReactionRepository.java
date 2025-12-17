package com.example.demo.repository;

import com.example.demo.model.Reaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ReactionRepository extends JpaRepository<Reaction, Long> {
    Optional<Reaction> findByUserIdAndTargetId(Long userId, Long targetId);
    Long countByTargetId(Long targetId);
}
