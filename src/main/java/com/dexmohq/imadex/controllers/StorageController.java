package com.dexmohq.imadex.controllers;

import com.dexmohq.imadex.storage.StorageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/storage")
public class StorageController {

    private final StorageService storageService;

    @Autowired
    public StorageController(StorageService storageService) {
        this.storageService = storageService;
    }

    @PostMapping("/upload")
    public ResponseEntity<String> uploadFile(@RequestParam("file") MultipartFile file) {
        try {
            storageService.store(file);
            return ResponseEntity.ok("Successfully uploaded");
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED)
                    .body("Failed to upload '" + file.getOriginalFilename() + "'");
        }
    }

    @GetMapping
    public ResponseEntity<Resource> get(@RequestParam("id") String id) {
        try {
            final Resource file = storageService.load(id);
            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION,
                            "attachment; filename=\"" + file.getFilename() + "\"")
                    .body(file);
        } catch (IOException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @DeleteMapping
    public ResponseEntity<String> delete(@RequestParam("id") String id) {
        try {
            storageService.delete(id);
            return ResponseEntity.ok("Successfully deleted");
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED)
                    .body("Deletion failed");
        }
    }


}
