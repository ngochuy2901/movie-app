package com.example.demo.configuration;

import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

import java.io.File;

@Configuration
public class FileStorageConfig {

    @Value("${file.upload-dir-videos}")
    private String uploadVideosDir;

    @Value("${file.upload-dir-images}")
    private String uploadImagesDir;

    @PostConstruct
    public void init() {
        File videoDirectory = new File(uploadVideosDir);
        File imageDirectory = new File(uploadVideosDir);
        if (!videoDirectory.exists()) {
            videoDirectory.mkdirs();
        }
        if (!imageDirectory.exists()) {
            imageDirectory.mkdirs();
        }
    }
}