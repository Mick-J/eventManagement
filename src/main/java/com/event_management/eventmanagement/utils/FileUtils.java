package com.event_management.eventmanagement.utils;

import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

public class FileUtils {

    public static boolean isFileSent(MultipartFile file) {
        return file != null && !file.isEmpty();
    }

    public static void uploadFile(String uploadDir, String storeFileNewName, MultipartFile file) throws IOException {
        // Convert uploadDir to Path object
        Path uploadPath = Paths.get(uploadDir);

        // Create directories if they do not exist
        if (Files.notExists(uploadPath)) {
            Files.createDirectories(uploadPath);
        }

        // Define the target file path (using resolve to safely join paths)
        Path targetPath = uploadPath.resolve(storeFileNewName);

        // Copy the file to the target path, replacing existing file if needed
        try (InputStream inputStream = file.getInputStream()) {
            Files.copy(inputStream, targetPath, StandardCopyOption.REPLACE_EXISTING);
        }
    }
}