package com.dexmohq.imadex.tag.clarifai;

import clarifai2.api.ClarifaiBuilder;
import clarifai2.api.ClarifaiClient;
import clarifai2.dto.input.ClarifaiInput;
import clarifai2.dto.model.output.ClarifaiOutput;
import clarifai2.dto.prediction.Concept;
import com.dexmohq.imadex.tag.Tag;
import com.dexmohq.imadex.tag.TaggingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ClarifaiTaggingService implements TaggingService {

    private final ClarifaiProperties clarifaiProperties;

    @Autowired
    public ClarifaiTaggingService(ClarifaiProperties clarifaiProperties) {
        this.clarifaiProperties = clarifaiProperties;
    }

    @Override
    public List<Tag> extractTags(Resource image) {
        final ClarifaiClient client = new ClarifaiBuilder(clarifaiProperties.getApiKey())
                .buildSync();
        try {
            final List<ClarifaiOutput<Concept>> outputs = client.getDefaultModels().generalModel()
                    .predict()
                    .withInputs(ClarifaiInput.forImage(image.getFile()))
                    .executeSync()
                    .get();

            assert outputs.size() == 1;
            final ClarifaiOutput<Concept> output = outputs.get(0);

            return output.data().stream().map(ClarifaiTag::new).collect(Collectors.toList());

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
