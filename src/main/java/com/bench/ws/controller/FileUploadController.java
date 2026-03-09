package com.bench.ws.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.Set;
import java.util.UUID;

@RestController
@CrossOrigin(origins = "*")
public class FileUploadController {

    private static final String UPLOAD_DIR = System.getProperty("user.dir") + "/uploads/";

    // Allowed MIME types — reject everything else
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

        // 1. Reject empty uploads
        if (file.isEmpty()) {
            return ResponseEntity.badRequest().body("File is empty");
        }

        // 2. Validate MIME type
        String contentType = file.getContentType();
        if (contentType == null || !ALLOWED_TYPES.contains(contentType)) {
            return ResponseEntity.badRequest().body("File type not allowed: " + contentType);
        }

        // 3. Sanitize original filename — getOriginalFilename() can be null
        String originalName = file.getOriginalFilename();
        String safeName = (originalName != null ? originalName : "file")
                .replaceAll("[^a-zA-Z0-9._-]", "_"); // strip anything unsafe

        // 4. Build a unique filename
        String fileName = UUID.randomUUID() + "_" + safeName;

        // 5. Create uploads directory if it doesn't exist
        File directory = new File(UPLOAD_DIR);
        if (!directory.exists()) {
            directory.mkdirs();
        }

        // 6. transferTo() requires an absolute path — without getAbsoluteFile()
        //    it resolves relative to the JVM working directory and can silently fail
        File destination = new File(UPLOAD_DIR + fileName).getAbsoluteFile();
        file.transferTo(destination);

        // 7. Return the public URL the frontend can use directly
        String fileUrl = "http://localhost:8080/uploads/" + fileName;
        return ResponseEntity.ok(fileUrl);
    }
}