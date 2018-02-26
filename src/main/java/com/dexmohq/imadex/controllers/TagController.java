package com.dexmohq.imadex.controllers;

import com.dexmohq.imadex.data.*;
import com.dexmohq.imadex.storage.StorageItem;
import com.dexmohq.imadex.storage.StorageService;
import com.dexmohq.imadex.tag.TaggingServiceProvider;
import com.dexmohq.imadex.tag.TaggingSourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Future;
import java.util.stream.Collectors;

import static com.dexmohq.imadex.auth.IdTokenEnhancer.getUserId;
import static java.util.stream.Collectors.toList;

@RestController
@RequestMapping("/tag")
@PreAuthorize("hasAuthority('USER')")
public class TagController {

    private static final String CUSTOM_TAG_SOURCE = "custom";

    private final StorageService storageService;
    private final TaggingServiceProvider taggingServiceProvider;
    private final TaggedImageRepository taggedImageRepository;

    @Autowired
    public TagController(StorageService storageService, TaggingServiceProvider taggingServiceProvider, TaggedImageRepository taggedImageRepository) {
//        this.tagRepository = tagRepository;
        this.storageService = storageService;
        this.taggingServiceProvider = taggingServiceProvider;
        this.taggedImageRepository = taggedImageRepository;
    }

    @GetMapping("/list")
    public List<TaggedStorageItem> list(@RequestParam("page") int page,
                                        @RequestParam(value = "pageSize", defaultValue = "20") int pageSize,
                                        OAuth2Authentication authentication) throws IOException {
        final String userId = getUserId(authentication);
        return storageService.listFiles(userId, page, pageSize).map(item -> {
            final TaggedImage taggedImage = taggedImageRepository.findByUserIdAndFilename(userId, item.getFilename());
            final Set<TagDocument> tags = taggedImage == null ? null : taggedImage.getTags();
            return new TaggedStorageItem(item, tags);
        }).collect(toList());
    }

    @PostMapping("/add")
    public ResponseEntity<?> addTag(@RequestParam("image") String image,
                                    @RequestParam("tag") String tag,
                                    OAuth2Authentication authentication) {
        final String userId = getUserId(authentication);
        if (!storageService.exists(userId, image)) {
            return ResponseEntity.notFound().build();
        }
        TaggedImage taggedImage = taggedImageRepository.findByUserIdAndFilename(userId, image);
        if (taggedImage == null) {
            taggedImage = TaggedImage.builder().build();
            taggedImage.setTags(Collections.singleton(TagDocument.create(tag, CUSTOM_TAG_SOURCE)));
        } else {
            final Set<TagDocument> tags = taggedImage.getTags();
            if (tags == null || tags.isEmpty()) {
                taggedImage.setTags(Collections.singleton(TagDocument.create(tag, CUSTOM_TAG_SOURCE)));
            } else {
                final Optional<TagDocument> optionalDocument = tags.stream()
                        .filter(t -> t.getTag().equals(tag.toLowerCase()))
                        .findAny();
                if (optionalDocument.isPresent()) {
                    final TagDocument document = optionalDocument.get();
                    final Map<String, TagQuality> qualities = document.getQualities();
                    if (qualities != null) {
                        qualities.putIfAbsent(CUSTOM_TAG_SOURCE, new TagQuality());
                    } else {
                        document.setQualities(Collections.singletonMap(CUSTOM_TAG_SOURCE, new TagQuality()));
                    }
                } else {
                    tags.add(TagDocument.create(tag, CUSTOM_TAG_SOURCE));
                }
            }
        }
        taggedImageRepository.save(taggedImage);
        return ResponseEntity.ok("Successful");
    }

    @PostMapping("/compute")
    public Future<ResponseEntity<?>> compute(@RequestParam("source") String source,
                                             @RequestParam("image") String image,
                                             OAuth2Authentication authentication)
            throws TaggingSourceNotFoundException, IOException {
        final String userId = getUserId(authentication);
        final TaggedImage existing = taggedImageRepository.findByUserIdAndFilename(userId, image);
        if (existing != null && existing.getAlreadyTaggedBy() != null
                && existing.getAlreadyTaggedBy().contains(source.toLowerCase())) {
            return CompletableFuture.completedFuture(
                    ResponseEntity.status(HttpStatus.CONFLICT).body("Already computed"));
        }
        final Resource resource = storageService.load(userId, image);
        final long fileSize = resource.contentLength();
        return taggingServiceProvider.getTaggingService(source)
                .extractTagsAsync(resource)
                .thenApply(stream -> new TaggedStorageItem(
                        new StorageItem(image, fileSize),
                        stream.map(tag -> TagDocument.create(tag, source)).collect(Collectors.toSet())))
                .whenComplete((taggedStorageItem, throwable) -> {
                    TaggedImage taggedImage = existing;
                    if (taggedImage == null) {
                        taggedImage = TaggedImage.builder().build();
                        taggedImage.setUserId(userId);
                        taggedImage.setFilename(image);
                    }
                    Set<TagDocument> tags = taggedImage.getTags();
                    if (tags == null) {
                        tags = new HashSet<>();
                        taggedImage.setTags(tags);
                    }
                    for (TagDocument tagDocument : taggedStorageItem.getTags()) {
                        final Optional<TagDocument> optionalDocument = tags.stream().filter(td -> td.getTag().equalsIgnoreCase(tagDocument.getTag())).findAny();
                        if (optionalDocument.isPresent()) {
                            final TagDocument document = optionalDocument.get();
                            final Map<String, TagQuality> qualities = document.getQualities();
                            if (qualities == null) {
                                document.setQualities(tagDocument.getQualities());
                            } else {
                                qualities.putAll(tagDocument.getQualities());
                            }
                        } else {
                            tags.add(tagDocument);
                        }
                    }
                    tags.addAll(taggedStorageItem.getTags());
                    taggedImage.getAlreadyTaggedBy().add(source);
                    taggedImageRepository.save(taggedImage);
                })
                .thenApply(ResponseEntity::ok);
    }

}
