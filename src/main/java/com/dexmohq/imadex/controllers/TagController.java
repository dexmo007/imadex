package com.dexmohq.imadex.controllers;

import com.dexmohq.imadex.data.TagDocument;
import com.dexmohq.imadex.data.TagRepository;
import com.dexmohq.imadex.data.TaggedImage;
import com.dexmohq.imadex.storage.StorageItem;
import com.dexmohq.imadex.storage.StorageService;
import com.dexmohq.imadex.tag.TaggingServiceProvider;
import com.dexmohq.imadex.tag.TaggingSourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.Future;
import java.util.stream.Collectors;

import static com.dexmohq.imadex.auth.IdTokenEnhancer.getUserId;

@RestController
@RequestMapping("/tag")
@PreAuthorize("hasAuthority('USER')")
public class TagController {

    private final TagRepository tagRepository;
    private final StorageService storageService;
    private final TaggingServiceProvider taggingServiceProvider;


    @Autowired
    public TagController(TagRepository tagRepository, StorageService storageService, TaggingServiceProvider taggingServiceProvider) {
        this.tagRepository = tagRepository;
        this.storageService = storageService;
        this.taggingServiceProvider = taggingServiceProvider;
    }

    @GetMapping("/list")
    public List<TaggedStorageItem> list(@RequestParam("page") int page,
                                        @RequestParam(value = "pageSize", defaultValue = "20") int pageSize,
                                        OAuth2Authentication authentication) throws IOException {//todo extract user id
        final String userId = getUserId(authentication);
        return storageService.listFiles(userId, page, pageSize).map(item -> {
            final TaggedImage taggedImage = tagRepository.findOne(userId + item.getFilename());
            final Set<TagDocument> tags = taggedImage == null ? null : taggedImage.getTags();
            return new TaggedStorageItem(item, tags);
        }).collect(Collectors.toList());
    }

    @PostMapping("/add")
    public ResponseEntity<?> addTag(@RequestParam("image") String image,
                                    @RequestParam("tag") String tag,
                                    OAuth2Authentication authentication) {
        final String userId = getUserId(authentication);
        if (!storageService.exists(userId, image)) {
            return ResponseEntity.notFound().build();
        }
        TaggedImage taggedImage = tagRepository.findOne(userId + image);
        if (taggedImage == null) {
            taggedImage = new TaggedImage(userId, image);
        }
        final TagDocument tagDocument = new TagDocument();
        tagDocument.setSource("custom");
        tagDocument.setTag(tag);
        Set<TagDocument> tags = taggedImage.getTags();
        if (tags == null) {
            tags = new HashSet<>();
            taggedImage.setTags(tags);
        }
        tags.add(tagDocument);
        tagRepository.save(taggedImage);
        return ResponseEntity.ok("Successful");
    }

    @PostMapping("/compute")
    public Future<TaggedStorageItem> compute(@RequestParam("source") String source,
                                             @RequestParam("image") String image,
                                             OAuth2Authentication authentication)
            throws TaggingSourceNotFoundException, IOException {
        final String userId = getUserId(authentication);
        final Resource resource = storageService.load(userId, image);
        final long fileSize = resource.contentLength();
        return taggingServiceProvider.getTaggingService(source)
                .extractTagsAsync(resource)
                .thenApply(stream -> new TaggedStorageItem(
                        new StorageItem(image, fileSize),
                        stream.map(tag -> TagDocument.create(tag, source)).collect(Collectors.toSet())))
                .whenComplete((taggedStorageItem, throwable) -> {
                    TaggedImage taggedImage = tagRepository.findOne(userId + image);
                    if (taggedImage == null) {
                        taggedImage = new TaggedImage(userId, image);
                    }
                    Set<TagDocument> tags = taggedImage.getTags();
                    if (tags == null) {
                        tags = new HashSet<>();
                        taggedImage.setTags(tags);
                    }
                    tags.addAll(taggedStorageItem.getTags());
                    tagRepository.save(taggedImage);
                });
    }

}
