package com.example.demo.controller;

import com.example.demo.model.Image;
import com.example.demo.repository.ImageRepository;
import com.example.demo.security.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("image")
public class ImageController {
    private final ImageRepository imageRepository;
    private final JwtService jwtService;
    @Value("${file.upload-dir-images}")
    private String IMG_DIR;

    @GetMapping("load_user_avatar/{fileName}")
    public ResponseEntity<Resource> loadUserAvatar(@RequestHeader("Authorization") String authHeader,
                                                   @PathVariable String fileName) throws IOException {
        String token = jwtService.getTokenFromAuthHeader(authHeader);
        if (token == null) {
            return ResponseEntity.status(401).build(); // Unauthorized
        }
        // 2. Tạo đường dẫn file
        File file = new File(IMG_DIR + "/" + fileName);
        if (!file.exists()) {
            return ResponseEntity.notFound().build();
        }

        // 3. Chuyển sang Resource
        FileSystemResource resource = new FileSystemResource(file);

        // 4. Xác định Content-Type (image/jpeg/png/webp...)
        String contentType = Files.probeContentType(file.toPath());
        if (contentType == null) {
            contentType = "application/octet-stream";
        }

        // 5. Trả về response
        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(contentType))
                .body(resource);
    }

    @GetMapping("load_public_image/{fileName}")
    public ResponseEntity<Resource> loadPublicImage(
            @PathVariable String fileName) throws IOException {

        // 2. Tạo đường dẫn file
        File file = new File(IMG_DIR + "/" + fileName);
        if (!file.exists()) {
            return ResponseEntity.notFound().build();
        }

        // 3. Chuyển sang Resource
        FileSystemResource resource = new FileSystemResource(file);

        // 4. Xác định Content-Type (image/jpeg/png/webp...)
        String contentType = Files.probeContentType(file.toPath());
        if (contentType == null) {
            contentType = "application/octet-stream";
        }

        // 5. Trả về response
        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(contentType))
                .body(resource);
    }

}
