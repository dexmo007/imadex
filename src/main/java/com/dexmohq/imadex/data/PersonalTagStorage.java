package com.dexmohq.imadex.data;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Document(collection = "tags")
@Data
public class PersonalTagStorage {
    @Id
    private String userId;

    private List<TaggedImage2> images;



}
