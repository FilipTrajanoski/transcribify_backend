package com.example.transcribify.service.impl;

import com.example.transcribify.service.ImageService;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

@Service
public class ImageServiceImpl implements ImageService {

    String uploadDirectory = "uploads/";
    String defaultUserImage = "default-avatar.webp";

    @Override
    public String saveImage(MultipartFile file) throws IOException {
        Path uploadPath = Paths.get(uploadDirectory);
        if (!Files.exists(uploadPath)) {
            Files.createDirectories(uploadPath);
        }

        String imagePath = uploadDirectory + defaultUserImage;

        if (file != null && !file.isEmpty()) {
            String fileName = UUID.randomUUID() + "_" + file.getOriginalFilename();
            Path path = Paths.get(uploadDirectory, fileName);
            Files.copy(file.getInputStream(), path);
            imagePath = path.toString();

        }

        return imagePath;
    }
}
