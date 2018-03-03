package com.dexmohq.imadex.recognize.data;

import org.springframework.data.mongodb.repository.MongoRepository;

public interface PersonalFaceRecognizerRepository extends MongoRepository<PersonalFaceRecognizer, String> {

    PersonalFaceRecognizer findByUserId(String userId);

}
