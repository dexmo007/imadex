package com.dexmohq.imadex.auth;

import org.springframework.data.mongodb.repository.MongoRepository;

public interface MongoClientDetailsRepository extends MongoRepository<MongoClientDetails, String> {
}
