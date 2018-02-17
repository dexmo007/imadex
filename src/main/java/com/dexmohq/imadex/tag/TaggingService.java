package com.dexmohq.imadex.tag;

import org.springframework.core.io.Resource;

import java.io.IOException;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;
import java.util.concurrent.Future;
import java.util.stream.Stream;

public interface TaggingService {

    String getSource();

    Stream<? extends Tag> extractTags(Resource image) throws IOException, TaggingProcessingException;

    CompletableFuture<Stream<? extends Tag>> extractTagsAsync(Resource image) throws IOException;

}
