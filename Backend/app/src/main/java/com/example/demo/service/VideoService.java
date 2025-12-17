package com.example.demo.service;

import com.example.demo.model.Video;
import com.example.demo.repository.VideoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;

@Service
@RequiredArgsConstructor
public class VideoService {
    private final VideoRepository videoRepository;
    private final UserService userService;
    public List<Video> getAllVideos() {
        return videoRepository.findAll();
    }

    @Value("${file.upload-dir-videos}")
    private String uploadDir;

    public String saveVideo(MultipartFile file, Video video, String token) throws IOException {
        if (file.isEmpty()) {
            throw new RuntimeException("File rỗng!");
        }
        String fileName = System.currentTimeMillis() + getFileExtension(file.getOriginalFilename());
        Long userId = userService.getUserIdByToken(token);
        video.setUserId(userId);
        //upload ten file
        Path targetPath = Paths.get(uploadDir).resolve(fileName).normalize();
        Files.copy(file.getInputStream(), targetPath, StandardCopyOption.REPLACE_EXISTING);
        video.setUrl(fileName);
        videoRepository.save(video);
        // Trả về đường dẫn hoặc tên file để lưu trong DB
        return fileName;
    }

    private String getFileExtension(String fileName) {
        if (fileName == null || fileName.isEmpty()) {
            return ""; // trả về rỗng nếu fileName null hoặc rỗng
        }

        int dotIndex = fileName.lastIndexOf('.');
        if (dotIndex == -1 || dotIndex == fileName.length() - 1) {
            return ""; // không có dấu chấm hoặc chấm cuối cùng → không có extension
        }

        return fileName.substring(dotIndex); // bao gồm dấu chấm, ví dụ ".mp4"
    }
}


