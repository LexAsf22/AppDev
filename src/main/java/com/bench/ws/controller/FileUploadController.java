package com.bench.ws.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.Set;
import java.util.UUID;

@RestController
@CrossOrigin(origins = "*") // Already fine
public class FileUploadController {

    private static final String UPLOAD_DIR = System.getProperty("user.dir") + "/uploads/";

    private static final Set<String> ALLOWED_TYPES = Set.of(
        "image/jpeg", "image/png", "image/gif", "image/webp",
        "audio/webm", "audio/ogg", "audio/mpeg",
        "application/pdf",
        "text/plain",
        "application/msword",
        "application/vnd.openxmlformats-officedocument.wordprocessingml.document",
        "application/vnd.ms-excel",
        "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"
    );

    @PostMapping("/upload")
    public ResponseEntity<String> uploadFile(@RequestParam("file") MultipartFile file) throws IOException {
        if (file.isEmpty()) return ResponseEntity.badRequest().body("File is empty");

        String contentType = file.getContentType();
        if (contentType == null || !ALLOWED_TYPES.contains(contentType)) {
            return ResponseEntity.badRequest().body("File type not allowed: " + contentType);
        }

        String originalName = file.getOriginalFilename();
        String safeName = (originalName != null ? originalName : "file")
                .replaceAll("[^a-zA-Z0-9._-]", "_");

        String fileName = UUID.randomUUID() + "_" + safeName;

        File directory = new File(UPLOAD_DIR);
        if (!directory.exists()) directory.mkdirs();

        File destination = new File(directory, fileName).getAbsoluteFile();
        file.transferTo(destination);

        // Fix: return backend IP instead of localhost for LAN access
        String fileUrl = "http://192.168.195.90:8080/uploads/" + fileName;
        return ResponseEntity.ok(fileUrl);
    }
}