package com.example.demo.service;

import com.example.demo.model.Image;
import com.example.demo.repository.ImageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

@RequiredArgsConstructor
@Service
public class ImageService {
    private final ImageRepository imageRepository;

    @Value("${file.upload-dir-images}")
    private String uploadDir;

    public String saveImage(MultipartFile fileImg, Image image) throws IOException {
        // kiem tra file anh
        if (fileImg == null || fileImg.isEmpty()) {
            throw new RuntimeException("File ảnh không hợp lệ!");
        }
        String contentType = fileImg.getContentType();
        if (contentType == null || !contentType.startsWith("image/")) {
            throw new RuntimeException("File tải lên không phải là ảnh!");
        }
        //
        String fileName = System.currentTimeMillis() + getFileExtension(fileImg.getOriginalFilename());
        Path targetPath = Paths.get(uploadDir).resolve(fileName).normalize();
        Files.createDirectories(targetPath.getParent()); // đảm bảo thư mục tồn tại
        Files.copy(fileImg.getInputStream(), targetPath, StandardCopyOption.REPLACE_EXISTING);
        image.setUrl(fileName);
        imageRepository.save(image);
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
