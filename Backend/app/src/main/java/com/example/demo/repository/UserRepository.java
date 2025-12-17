package com.example.demo.repository;

import com.example.demo.dto.UserDto;
import com.example.demo.model.User;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    boolean existsByEmail(String email);
    boolean existsByUsername(String username);
    boolean existsByPhoneNumber(String phoneNumber);
    Optional<User> findByUsername(String username);
    // Lấy UserDto từ id
    @Query("SELECT new com.example.demo.dto.UserDto(u.id, u.fullName, u.email, u.phoneNumber, u.username, u.imgUrl, u.gender, u.dateOfBirth) " +
            "FROM User u WHERE u.id = :id")
    Optional<UserDto> findUserDtoById(Long id);

    // Nếu muốn lấy UserDto theo username
    @Query("SELECT new com.example.demo.dto.UserDto(u.id, u.fullName, u.email, u.phoneNumber, u.username, u.imgUrl, u.gender, u.dateOfBirth) " +
            "FROM User u WHERE u.username = :username")
    Optional<UserDto> findUserDtoByUsername(String username);

    //cap nhat avata bang username
    @Modifying
    @Transactional
    @Query("UPDATE User u SET u.imgUrl = :avatarUrl WHERE u.username = :username")
    int updateUserAvatar(String username, String avatarUrl);

}
