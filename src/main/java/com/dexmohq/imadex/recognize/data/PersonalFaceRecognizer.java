package com.dexmohq.imadex.recognize.data;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "face_recognizers")
@Getter
@Setter
public class PersonalFaceRecognizer {
    @Id
    private String id;
    @Indexed(unique = true)
    private String userId;
    private StoredEigenFaceRecognizer storedEigenFaceRecognizer;
}
