package com.dexmohq.imadex.tag.azure;

import com.dexmohq.imadex.tag.Tag;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@Getter
@Setter
@ToString
public class AzureCognitiveVisionTagResponse {

    @JsonDeserialize(contentAs = AzureCognitiveVisionTag.class)
    private List<AzureCognitiveVisionTag> tags;

    private String requestId;

    private Metadata metadata;

    @Getter
    @Setter
    @ToString
    public static class Metadata {
        private int height;
        private int width;
        private String format;
    }

}
