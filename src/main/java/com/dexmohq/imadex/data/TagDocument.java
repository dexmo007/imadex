package com.dexmohq.imadex.data;

import com.dexmohq.imadex.tag.Tag;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;

@Getter
@Setter
public class TagDocument implements Tag {

    private String tag;
    private Float score;
    private Float confidence;
    private String source;

    public static TagDocument create(@NonNull Tag tag, @NonNull String source) {
        final TagDocument tagDocument = new TagDocument();
        tagDocument.setTag(tag.getTag());
        tagDocument.setScore(tag.getScore());
        tagDocument.setConfidence(tag.getConfidence());
        tagDocument.setSource(source);
        return tagDocument;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        TagDocument that = (TagDocument) o;

        if (tag != null ? !tag.equals(that.tag) : that.tag != null) return false;
        return source != null ? source.equals(that.source) : that.source == null;
    }

    @Override
    public int hashCode() {
        int result = tag != null ? tag.hashCode() : 0;
        result = 31 * result + (source != null ? source.hashCode() : 0);
        return result;
    }
}
