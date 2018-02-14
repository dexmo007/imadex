package com.dexmohq.imadex.tag.ibm;

import com.dexmohq.imadex.tag.Tag;
import com.ibm.watson.developer_cloud.visual_recognition.v3.model.ClassResult;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class IbmWatsonVisualRecognitionTag implements Tag {

    private final ClassResult classResult;

    @Override
    public String getTag() {
        return classResult.getClassName();
    }

    @Override
    public Float getScore() {
        return classResult.getScore();
    }

    @Override
    public Float getConfidence() {
        return null;
    }
}
