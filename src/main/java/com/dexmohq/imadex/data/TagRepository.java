package com.dexmohq.imadex.data;

import org.springframework.data.mongodb.repository.MongoRepository;

public interface TagRepository extends MongoRepository<TaggedImage, String> {
}
