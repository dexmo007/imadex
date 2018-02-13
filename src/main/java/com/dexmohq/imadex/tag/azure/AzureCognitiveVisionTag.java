package com.dexmohq.imadex.tag.azure;

import com.dexmohq.imadex.tag.Tag;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Setter;
import lombok.ToString;


@Setter
@ToString
public class AzureCognitiveVisionTag implements Tag {
    @JsonProperty("name")
    private String tag;
    private float confidence;

    @Override
    public String getTag() {
        return tag;
    }

    @Override
    @JsonIgnore
    public Float getScore() {
        return null;
    }

    @Override
    public Float getConfidence() {
        return confidence;
    }

}
