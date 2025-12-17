package com.example.demo.controller;


import com.example.demo.model.Image;
import com.example.demo.model.Video;
import com.example.demo.security.JwtService;
import com.example.demo.service.ImageService;
import com.example.demo.service.UserService;
import com.example.demo.service.VideoService;
import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.web.multipart.MultipartFile;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("video")
public class VideoController {
    private final VideoService videoService;
    private final JwtService jwtService;
    private final ImageService imageService;
    private final UserService userService;
    @Value("${file.upload-dir-videos}")
    private String VIDEO_DIR;


    @GetMapping("all_videos")
    public ResponseEntity<List<Video>> fillAllVideos() {
        return ResponseEntity.ok(videoService.getAllVideos());
    }

@GetMapping("/play_video/{fileName}")
public ResponseEntity<Resource> streamVideo(
        @PathVariable String fileName,
        @RequestHeader(value = "Range", required = false) String rangeHeader
) throws IOException {

    File file = new File(VIDEO_DIR + "/" + fileName);

    if (!file.exists()) {
        return ResponseEntity.notFound().build();
    }

    long fileLength = file.length();
    long rangeStart = 0;
    long rangeEnd = fileLength - 1;

    if (rangeHeader != null && rangeHeader.startsWith("bytes=")) {
        String[] ranges = rangeHeader.substring(6).split("-");
        rangeStart = Long.parseLong(ranges[0]);
        if (ranges.length > 1 && !ranges[1].isEmpty()) {
            rangeEnd = Long.parseLong(ranges[1]);
        }
    }

    long contentLength = rangeEnd - rangeStart + 1;

    RandomAccessFile videoFile = new RandomAccessFile(file, "r");
    videoFile.seek(rangeStart);

    InputStreamResource inputStreamResource = new InputStreamResource(
            new FileInputStream(videoFile.getFD())
    );

    HttpHeaders headers = new HttpHeaders();
    headers.set(HttpHeaders.ACCEPT_RANGES, "bytes");
    headers.set(HttpHeaders.CONTENT_TYPE, "video/mp4");
    headers.set(HttpHeaders.CONTENT_LENGTH, String.valueOf(contentLength));
    headers.set(
            HttpHeaders.CONTENT_RANGE,
            "bytes " + rangeStart + "-" + rangeEnd + "/" + fileLength
    );

    return ResponseEntity
            .status(rangeHeader == null ? 200 : 206)
            .headers(headers)
            .body(inputStreamResource);
}


    //upload video
    @PostMapping("/upload")
    public ResponseEntity<?> uploadVideo(@RequestParam("file") MultipartFile file, @RequestParam("thumbnail") MultipartFile thumbnailFile, @ModelAttribute Video video, @RequestHeader("Authorization") String authHeader) {
        try {
            String token = jwtService.getTokenFromAuthHeader(authHeader);
            if (token == null) {
                return ResponseEntity.ok(Map.of(
                        "message", "Upload không thành công: Token khong hop le"
                ));
            }
            Image image = new Image();
            image.setUserId(userService.getUserIdByToken(token));
            String thumbnailUrl = imageService.saveImage(thumbnailFile, image);
            System.out.println("image url" + thumbnailUrl);
            video.setUrlThumbnail(thumbnailUrl);
            System.out.println("image url" + video.getUrlThumbnail());
            String fileName = videoService.saveVideo(file, video, token);

            return ResponseEntity.ok(Map.of(
                    "message", "Upload thành công",
                    "fileName", fileName
            ));
        } catch (Exception e) {
            return ResponseEntity.ok(Map.of(
                    "message", "Upload khong thanh cong",
                    "error", e.getMessage()
            ));
        }
    }
}
