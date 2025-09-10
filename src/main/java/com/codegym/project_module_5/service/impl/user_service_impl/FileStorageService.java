package com.codegym.project_module_5.service.impl.user_service_impl;

import java.io.IOException;
import java.nio.file.*;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
@Service
public class FileStorageService {

    @Value("${upload.avatar.dir}")
    private String avatarUploadDir;

    @Value("${upload.avatar.url}")
    private String avatarBaseUrl;

    public String saveAvatar(MultipartFile file) {
        if (file.isEmpty()) {
            return null;
        }
        try {
            String fileName = System.currentTimeMillis() + "_" + file.getOriginalFilename();
            Path uploadPath = Paths.get(avatarUploadDir);
            if (!Files.exists(uploadPath)) {
                Files.createDirectories(uploadPath);
            }
            Files.copy(file.getInputStream(), uploadPath.resolve(fileName), StandardCopyOption.REPLACE_EXISTING);
            System.out.println("Avatar saved to: " + uploadPath.resolve(fileName).toString());
            return avatarBaseUrl + fileName; // Chỉ lưu link vào DB
        } catch (IOException e) {
            System.err.println("Error saving avatar: " + e.getMessage());
            throw new RuntimeException("Could not store file: " + e.getMessage());
        }
    }
}
