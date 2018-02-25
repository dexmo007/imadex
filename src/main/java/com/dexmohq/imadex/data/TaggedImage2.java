package com.dexmohq.imadex.data;

import lombok.Data;

import java.util.Set;

@Data
public class TaggedImage2 {

    private String filename;

    private Set<String> alreadyTaggedBy;

    private Set<TagDocument> tags;
}
