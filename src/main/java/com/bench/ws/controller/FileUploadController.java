package com.bench.ws.controller;

import java.io.File;
import java.io.IOException;
import java.util.Set;
import java.util.UUID;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@CrossOrigin(origins = "*")
public class FileUploadController {

    private static final String UPLOAD_DIR = System.getProperty("user.dir") + "/uploads/";

    private static final Set<String> ALLOWED_TYPES = Set.of(
        "image/jpeg", "image/png", "image/gif", "image/webp",
        "audio/webm", "audio/ogg", "audio/mpeg",
        "video/mp4", "video/webm", "video/ogg", "video/quicktime", "video/x-matroska",
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

        String fileUrl = "http://192.168.100.127:8080/uploads/" + fileName;
        return ResponseEntity.ok(fileUrl);
    }
}