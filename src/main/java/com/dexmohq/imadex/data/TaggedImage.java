package com.dexmohq.imadex.data;

import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Set;

@Document(collection = "tags")
@Data
@Builder
public class TaggedImage {

    @Id
    private String id;
    private String userId;
    private String filename;
    private Set<String> alreadyTaggedBy;
    private Set<TagDocument> tags;

}
