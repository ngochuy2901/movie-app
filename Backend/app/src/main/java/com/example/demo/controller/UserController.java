package com.example.demo.controller;

import com.example.demo.dto.LoginRequest;
import com.example.demo.dto.LoginResponse;
import com.example.demo.dto.RegisterResponse;
import com.example.demo.dto.UserDto;
import com.example.demo.model.Image;
import com.example.demo.model.User;
import com.example.demo.security.JwtService;
import com.example.demo.service.ImageService;
import com.example.demo.service.UserService;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Map;
import java.util.Optional;

@RequiredArgsConstructor
@RestController
@RequestMapping("user")
public class UserController {
    private final UserService userService;
    private final ImageService imageService;
    private final JwtService jwtService;


    @PostMapping("register")
    public ResponseEntity<RegisterResponse> register(@RequestBody User user) {
        return ResponseEntity.ok(userService.register(user));
    }

    @PostMapping("login")
    public ResponseEntity<LoginResponse> login(@RequestBody LoginRequest loginRequest) {
        return ResponseEntity.ok(userService.login(loginRequest));
    }

    @GetMapping("get_user_info_by_userid/{userId}")
    public Optional<UserDto> getUserInfoByUserId(@PathVariable Long userId) {
        return userService.getUserInfoById(userId);
    }


    @GetMapping("get_user_info")
    public ResponseEntity<UserDto> getUserInfo(
            @RequestHeader("Authorization") String authHeader) {

        // authHeader = "Bearer <token>"
        String token = null;
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            token = authHeader.substring(7);
        }

        if (token == null) {
            return ResponseEntity.status(401).build(); // Unauthorized
        }

        Optional<UserDto> userDto = userService.getUserDtoByToken(token);

        return userDto
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    //update avata
    @PostMapping("update_user_avatar")
    public ResponseEntity<?> uploadUserAvatar(@RequestParam("file") MultipartFile file, @RequestHeader("Authorization") String authHeader) {
        String token = jwtService.getTokenFromAuthHeader(authHeader);
        if (token == null) {
            return ResponseEntity.status(401).build(); // Unauthorized
        }

        try {
            Image image = new Image();
            image.setUserId(userService.getUserIdByToken(token));
            image.setPrivate(true);
            String urlImg = imageService.saveImage(file, image);
            int result = userService.updateUserAvatar(urlImg, token);
            return ResponseEntity.ok(Map.of(
                    "message", "Upload thành công",
                    "fileName", result
            ));
        } catch (Exception e) {
            return ResponseEntity.ok(Map.of(
                    "message", "Upload không thành công",
                    "fileName", e.getMessage()
            ));
        }
    }



}
