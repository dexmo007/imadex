package com.dexmohq.imadex.data;

import org.springframework.data.mongodb.repository.MongoRepository;

public interface TaggedImage3Repository extends MongoRepository<TaggedImage3, String> {
    TaggedImage3 findByUserIdAndFilename(String userId, String filename);
}
