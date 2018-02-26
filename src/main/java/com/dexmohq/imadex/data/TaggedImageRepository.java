package com.dexmohq.imadex.data;

import org.springframework.data.mongodb.repository.MongoRepository;

public interface TaggedImageRepository extends MongoRepository<TaggedImage, String> {
    TaggedImage findByUserIdAndFilename(String userId, String filename);
}
