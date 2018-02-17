package com.dexmohq.imadex.storage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.*;
import java.util.Spliterator;
import java.util.Spliterators;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

@Service
public class StorageService {

    private final StorageProperties storageProperties;

    @Autowired
    public StorageService(StorageProperties storageProperties) {
        this.storageProperties = storageProperties;
    }

    public Stream<StorageItem> listFiles(String userId, int page, int pageSize) throws IOException {
        if (page < 1) {
            throw new InvalidPaginationException("Page must be >1");
        }
        if (pageSize < 1) {
            throw new InvalidPaginationException("Page size must be >1");
        }
        final Path dir = storageProperties.getPath().resolve(userId);
        if (!Files.exists(dir)) {
            return Stream.empty();
        }
        final DirectoryStream<Path> paths = Files.newDirectoryStream(dir);
        return StreamSupport.stream(Spliterators.spliteratorUnknownSize(paths.iterator(), Spliterator.ORDERED), false)
                .skip((page - 1) * pageSize).limit(pageSize)
                .map(p -> new StorageItem(p.getFileName().toString(), p.toFile().length()));
    }

    private Path getPathFor(String userId, String filename) {
        return storageProperties.getPath().resolve(userId).resolve(filename);
    }

    public void store(String userId, MultipartFile file, boolean override) throws IOException {
        final Path destination = getPathFor(userId, file.getOriginalFilename());
        if (!override && Files.exists(destination)) {
            throw new FileAlreadyExistsException(destination.toString());
        }
        Files.createDirectories(storageProperties.getPath().resolve(userId));
        Files.copy(file.getInputStream(), destination, StandardCopyOption.REPLACE_EXISTING);
    }

    public boolean exists(String userId, String image) {
        final Path imagePath = getPathFor(userId, image);
        return Files.exists(imagePath);
    }

    public Resource load(String userId, String id) throws IOException {
        final Path file = getPathFor(userId, id);
        final UrlResource resource = new UrlResource(file.toUri());
        if (resource.exists() || resource.isReadable()) {
            return resource;
        } else {
            throw new FileNotFoundException(id);
        }
    }

    public void delete(String userId, String id) throws IOException {//todo delete tags
        Files.deleteIfExists(getPathFor(userId, id));
    }
}
