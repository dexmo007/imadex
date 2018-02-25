package com.dexmohq.imadex.data;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

public interface PersonalTagStorageRepository extends MongoRepository<PersonalTagStorage, String> {

    @Query("{userId: }")
    TaggedImage2 findTaggedImage(String userId, String image);

}
