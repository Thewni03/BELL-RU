package com.bellru.service;

import com.bellru.exception.BadRequestException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

@Service
public class FileStorageService {

    @Value("${app.upload-dir}")
    private String uploadDir;

    public String storeRoomImage(MultipartFile file) {
        if (file == null || file.isEmpty()) {
            throw new BadRequestException("No file provided");
        }
        try {
            Path roomsDir = Paths.get(uploadDir, "rooms");
            Files.createDirectories(roomsDir);

            String original = file.getOriginalFilename() != null ? file.getOriginalFilename() : "image";
            String extension = original.contains(".") ? original.substring(original.lastIndexOf('.')) : "";
            String filename = UUID.randomUUID() + extension;

            Path target = roomsDir.resolve(filename);
            Files.copy(file.getInputStream(), target);

            return "/uploads/rooms/" + filename;
        } catch (IOException e) {
            throw new RuntimeException("Failed to store image: " + e.getMessage(), e);
        }
    }
}
