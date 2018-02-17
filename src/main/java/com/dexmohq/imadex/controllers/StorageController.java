package com.dexmohq.imadex.controllers;

import com.dexmohq.imadex.image.FileCorruptedException;
import com.dexmohq.imadex.image.ImageFormatValidator;
import com.dexmohq.imadex.image.UnsupportedFormatException;
import com.dexmohq.imadex.storage.StorageItem;
import com.dexmohq.imadex.storage.StorageService;
import org.apache.tomcat.util.http.fileupload.FileUploadBase;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MaxUploadSizeExceededException;
import org.springframework.web.multipart.MultipartException;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.FileAlreadyExistsException;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/storage")
public class StorageController {

    public static final String USER_ID = "81e4b4f8-6682-449d-b357-4330eec55dff";
    private final StorageService storageService;
    private final ImageFormatValidator imageFormatValidator;

    @Autowired
    public StorageController(StorageService storageService, ImageFormatValidator imageFormatValidator) {
        this.storageService = storageService;
        this.imageFormatValidator = imageFormatValidator;
    }

    @GetMapping("/list")
    public List<StorageItem> listFiles(@RequestParam("page") int page, @RequestParam(value = "pageSize", defaultValue = "20") int pageSize) throws IOException {
        return storageService.listFiles(USER_ID, page, pageSize).collect(Collectors.toList());
    }

    @PostMapping("/upload")
    public ResponseEntity<String> uploadFile(@RequestParam("file") MultipartFile file,
                                             @RequestParam(name = "override", required = false) boolean override) throws FileCorruptedException, UnsupportedFormatException {
        try {
            imageFormatValidator.validateFormat(file);
            storageService.store("81e4b4f8-6682-449d-b357-4330eec55dff", file, override);
            return ResponseEntity.ok("Successfully uploaded");
        } catch (FileAlreadyExistsException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("File already exists. Override?");
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED)
                    .body("Failed to upload '" + file.getOriginalFilename() + "'");
        }
    }

    @GetMapping
    public ResponseEntity<Resource> get(@RequestParam("id") String id) {
        try {
            final Resource file = storageService.load("81e4b4f8-6682-449d-b357-4330eec55dff", id);
            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION,
                            "attachment; filename=\"" + file.getFilename() + "\"")
                    .body(file);
        } catch (IOException e) {
            return ResponseEntity.badRequest().build();//todo better handling
        }
    }

    @DeleteMapping
    public ResponseEntity<String> delete(@RequestParam("id") String id) {
        try {
            storageService.delete("81e4b4f8-6682-449d-b357-4330eec55dff", id);
            return ResponseEntity.ok("Successfully deleted");
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED)
                    .body("Deletion failed");
        }
    }

}
