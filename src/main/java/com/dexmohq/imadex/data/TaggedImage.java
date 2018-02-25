package com.dexmohq.imadex.data;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.UUID;

@Document(collection = "tagged_image")
@Getter
@Setter
@NoArgsConstructor
public class TaggedImage {

    @Id
    private String id;

    //todo more metadata

    private Set<String> alreadyTaggedBy;

    private Set<TagDocument> tags;

    public TaggedImage(String userId, String filename) {
        id = userId + filename;
    }

    public String getUserId() {
        return id.substring(0, 24);
    }

    public String getFilename() {
        return id.substring(24);
    }
}
