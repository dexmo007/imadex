package com.dexmohq.imadex.controllers;

import com.dexmohq.imadex.auth.stereotype.UserId;
import com.dexmohq.imadex.image.FileCorruptedException;
import com.dexmohq.imadex.image.ImageFormatValidator;
import com.dexmohq.imadex.image.UnsupportedFormatException;
import com.dexmohq.imadex.storage.StorageItem;
import com.dexmohq.imadex.storage.StorageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.FileAlreadyExistsException;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/storage")
@PreAuthorize("hasAuthority('USER')")
public class StorageController {

    private final StorageService storageService;
    private final ImageFormatValidator imageFormatValidator;

    @Autowired
    public StorageController(StorageService storageService, ImageFormatValidator imageFormatValidator) {
        this.storageService = storageService;
        this.imageFormatValidator = imageFormatValidator;
    }

    @GetMapping("/list")
    public List<StorageItem> listFiles(@RequestParam("page") int page,
                                       @RequestParam(value = "pageSize", defaultValue = "20") int pageSize,
                                       @UserId String userId) throws IOException {
        return storageService.listImages(userId, page, pageSize).collect(Collectors.toList());
    }

    @PostMapping("/upload")
    public ResponseEntity<String> uploadFile(@RequestParam("file") MultipartFile file,
                                             @RequestParam(name = "override", required = false) boolean override,
                                             @UserId String userId) throws FileCorruptedException, UnsupportedFormatException {
        try {
            imageFormatValidator.validateFormat(file);
            storageService.storeImage(userId, file, override);
            return ResponseEntity.ok("Successfully uploaded");
        } catch (FileAlreadyExistsException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("File already exists. Override?");
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED)
                    .body("Failed to upload '" + file.getOriginalFilename() + "'");
        }
    }

    @GetMapping
    public ResponseEntity<Resource> get(@RequestParam("id") String id,
                                        @UserId String userId) {
        try {
            final Resource file = storageService.loadImage(userId, id);
            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION,
                            "attachment; filename=\"" + file.getFilename() + "\"")
                    .body(file);
        } catch (IOException e) {
            return ResponseEntity.badRequest().build();//todo better handling
        }
    }

    @DeleteMapping
    public ResponseEntity<String> delete(@RequestParam("id") String id,
                                         @UserId String userId) {
        try {
            storageService.deleteImage(userId, id);
            return ResponseEntity.ok("Successfully deleted");
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED)
                    .body("Deletion failed");
        }
    }

}
