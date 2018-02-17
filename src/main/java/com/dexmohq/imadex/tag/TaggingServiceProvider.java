package com.dexmohq.imadex.tag;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class TaggingServiceProvider {

    private final Map<String, TaggingService> services;

    @Autowired
    public TaggingServiceProvider(List<TaggingService> taggingServices) {
        this.services = new HashMap<>();
        taggingServices.forEach(taggingService -> {
            final String source = taggingService.getSource();
            Assert.notNull(source, "Source must not be null");
            TaggingService duplicate;
            if ((duplicate = services.put(source, taggingService)) != null) {
                throw new IllegalStateException("Duplicate source identifiers for tagging services '"
                        + duplicate.getSource() + "' and '" + source + "'");
            }
        });
    }

    public TaggingService getTaggingService(String source) throws TaggingSourceNotFoundException {
        final TaggingService service = services.get(source);
        if (service == null) {
            throw new TaggingSourceNotFoundException(source);
        }
        return service;
    }
}
