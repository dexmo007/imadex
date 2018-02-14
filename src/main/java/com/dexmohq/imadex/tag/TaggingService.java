package com.dexmohq.imadex.tag;

import org.springframework.core.io.Resource;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.Future;
import java.util.stream.Stream;

public interface TaggingService {

    Stream<? extends Tag> extractTags(Resource image) throws IOException, TaggingProcessingException;

    Future<Stream<? extends Tag>> extractTagsAsync(Resource image) throws IOException;

}
