package com.dexmohq.imadex.data;

import com.dexmohq.imadex.tag.Tag;
import lombok.Data;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;

import java.util.HashMap;
import java.util.Map;

@Getter
@Setter
public class TagDocument {

    private String tag;
    private Map<String, TagQuality> qualities;

    public static TagDocument create(@NonNull Tag tag, @NonNull String source) {
        final TagDocument tagDocument = new TagDocument();
        tagDocument.setTag(tag.getTag());
        final Map<String, TagQuality> qualities = new HashMap<>();
        qualities.put(source, TagQuality.of(tag.getScore(),tag.getConfidence()));
        tagDocument.setQualities(qualities);
        return tagDocument;
    }

    public static TagDocument create(@NonNull String tag, @NonNull String source) {
        final TagDocument tagDocument = new TagDocument();
        tagDocument.setTag(tag);
        final Map<String, TagQuality> qualities = new HashMap<>();
        qualities.put(source, new TagQuality());
        tagDocument.setQualities(qualities);
        return tagDocument;
    }

    public void setTag(@NonNull String tag) {
        this.tag = tag.trim().toLowerCase();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof TagDocument)) return false;

        TagDocument that = (TagDocument) o;

        return tag.equals(that.tag);
    }

    @Override
    public int hashCode() {
        return tag.hashCode();
    }
}
