package com.dexmohq.imadex.tag.clarifai;

import clarifai2.dto.prediction.Concept;
import com.dexmohq.imadex.tag.Tag;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class ClarifaiTag implements Tag {

    private final Concept concept;

    @Override
    public String getTag() {
        return concept.name();
    }

    @Override
    public Float getScore() {
        return concept.value();
    }

    @Override
    public Float getConfidence() {
        return null;
    }
}
