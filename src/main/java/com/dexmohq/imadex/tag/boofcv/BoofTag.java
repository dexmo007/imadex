package com.dexmohq.imadex.tag.boofcv;

import com.dexmohq.imadex.tag.Tag;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

@ToString
@RequiredArgsConstructor
public class BoofTag implements Tag {
    private final String category;
    private final float score;
    @Override
    public String getTag() {
        return category;
    }

    @Override
    public Float getScore() {
        return score;
    }

    @Override
    public Float getConfidence() {
        return null;
    }
}
