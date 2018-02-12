package com.dexmohq.imadex.tag;

import org.springframework.core.io.Resource;

import java.util.List;

public interface TaggingService {

    List<Tag> extractTags(Resource image);

}
