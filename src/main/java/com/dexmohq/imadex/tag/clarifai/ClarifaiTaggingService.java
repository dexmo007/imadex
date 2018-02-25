package com.dexmohq.imadex.tag.clarifai;

import clarifai2.api.ClarifaiBuilder;
import clarifai2.api.ClarifaiClient;
import clarifai2.dto.input.ClarifaiInput;
import clarifai2.dto.input.ClarifaiInputValue;
import clarifai2.dto.model.output.ClarifaiOutput;
import clarifai2.dto.prediction.Concept;
import com.dexmohq.imadex.tag.Tag;
import com.dexmohq.imadex.tag.TaggingProcessingException;
import com.dexmohq.imadex.tag.TaggingService;
import com.dexmohq.imadex.util.Futures;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Future;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class ClarifaiTaggingService implements TaggingService {

    private final ClarifaiProperties clarifaiProperties;

    @Autowired
    public ClarifaiTaggingService(ClarifaiProperties clarifaiProperties) {
        this.clarifaiProperties = clarifaiProperties;
    }

    @Override
    public String getSource() {
        return "clarifai";
    }

    @Override
    public Stream<ClarifaiTag> extractTags(Resource image) throws IOException {
        final ClarifaiClient client = new ClarifaiBuilder(clarifaiProperties.getApiKey())
                .buildSync();
        final List<ClarifaiOutput<Concept>> outputs = client.getDefaultModels().generalModel()
                .predict()
                .withInputs(ClarifaiInput.forImage(image.getFile()))
                .executeSync()
                .get();

        assert outputs.size() == 1;
        final ClarifaiOutput<Concept> output = outputs.get(0);
        return output.data().stream().map(ClarifaiTag::new);
    }

    @Override
    public CompletableFuture<Stream<? extends Tag>> extractTagsAsync(Resource image) throws IOException {
        final ClarifaiInput input = ClarifaiInput.forImage(IOUtils.toByteArray(image.getInputStream()));
        final Future<ClarifaiClient> clientFuture = new ClarifaiBuilder(clarifaiProperties.getApiKey())
                .build();
        final CompletableFuture<ClarifaiClient> future = Futures.completable(clientFuture);
        return future.thenApply(client -> {
            final List<ClarifaiOutput<Concept>> outputs = client.getDefaultModels().generalModel()
                    .predict()
                    .withInputs(input)
                    .executeSync().get();
            if (outputs.size() != 1) {
                future.obtrudeException(new TaggingProcessingException());//todo may need fixing
                return null;
            }
            return outputs.get(0).data().stream().map(ClarifaiTag::new);
        });
    }
}
