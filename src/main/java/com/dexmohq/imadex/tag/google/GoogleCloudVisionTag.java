package com.dexmohq.imadex.tag.google;

import com.dexmohq.imadex.tag.Tag;
import com.google.cloud.vision.v1.EntityAnnotation;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class GoogleCloudVisionTag implements Tag {

    private final EntityAnnotation entityAnnotation;

    @Override
    public String getTag() {
        return entityAnnotation.getDescription();
    }

    @Override
    public Float getScore() {
        return entityAnnotation.getScore();
    }

    @Override
    public Float getConfidence() {
        return entityAnnotation.getConfidence();
    }
}
