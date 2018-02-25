package com.dexmohq.imadex.data;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "tags2")
@Data
public class TaggedImage3 {

    @Id
    private String id;
    private String userId;
    private String filename;
}
