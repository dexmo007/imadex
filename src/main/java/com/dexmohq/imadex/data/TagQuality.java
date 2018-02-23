package com.dexmohq.imadex.data;


import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TagQuality {
    private Float score;
    private Float confidence;

    static TagQuality of(Float score, Float confidence) {
        final TagQuality tagQuality = new TagQuality();
        tagQuality.setScore(score);
        tagQuality.setConfidence(confidence);
        return tagQuality;
    }
}
