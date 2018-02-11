package com.dexmohq.imadex.storage;

import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Service
public class StorageService {

    private static final Path STORAGE_ROOT = Paths.get("D:\\imadex-storage");//todo configurable for dev

    public void store(MultipartFile file) throws IOException {
        Files.copy(file.getInputStream(), STORAGE_ROOT.resolve(file.getOriginalFilename()));
    }

    public Resource load(String id) throws IOException {
        final Path file = STORAGE_ROOT.resolve(id);
        final UrlResource resource = new UrlResource(file.toUri());
        if (resource.exists() || resource.isReadable()) {
            return resource;
        } else {
            throw new FileNotFoundException(id);
        }
    }

    public void delete(String id) throws IOException {
        Files.deleteIfExists(STORAGE_ROOT.resolve(id));
    }
}
