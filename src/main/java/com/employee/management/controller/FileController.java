package com.employee.management.controller;

import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

@RestController
@RequestMapping("/api/files")
public class FileController {
    
    private static final String UPLOAD_DIR = "uploads";
    
    @GetMapping("/resumes/{filename:.+}")
    public ResponseEntity<Resource> serveFile(@PathVariable String filename) {
        try {
            Path file = Paths.get(UPLOAD_DIR, "resumes", filename);
            if (file == null) {
                return ResponseEntity.notFound().build();
            }
            java.net.URI uri = file.toUri();
            if (uri == null) {
                return ResponseEntity.notFound().build();
            }
            Resource resource = new UrlResource(uri);
            
            if (resource.exists() || resource.isReadable()) {
                MediaType mediaType = MediaType.APPLICATION_PDF;
                if (mediaType == null) {
                    mediaType = MediaType.APPLICATION_OCTET_STREAM;
                }
                return ResponseEntity.ok()
                        .contentType(mediaType)
                        .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=" + filename)
                        .body(resource);
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (IOException e) {
            return ResponseEntity.notFound().build();
        }
    }
}