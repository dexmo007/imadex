package com.dexmohq.imadex.data;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;
import java.util.Set;
import java.util.UUID;

@Document
@Getter
@Setter
@NoArgsConstructor
public class TaggedImage {

    @Id
    private String id;

    //todo more metadata

    private Set<TagDocument> tags;

    public TaggedImage(UUID userId, String filename) {
        id = userId.toString() + filename;
    }

    public UUID getUserId() {
        return UUID.fromString(id.substring(0, 36));
    }

    public String getFilename() {
        return id.substring(36);
    }
}
