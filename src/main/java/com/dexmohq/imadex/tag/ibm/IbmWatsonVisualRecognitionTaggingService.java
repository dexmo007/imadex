package com.dexmohq.imadex.tag.ibm;

import com.dexmohq.imadex.tag.Tag;
import com.dexmohq.imadex.tag.TaggingProcessingException;
import com.dexmohq.imadex.tag.TaggingService;
import com.ibm.watson.developer_cloud.http.ServiceCall;
import com.ibm.watson.developer_cloud.http.ServiceCallback;
import com.ibm.watson.developer_cloud.visual_recognition.v3.VisualRecognition;
import com.ibm.watson.developer_cloud.visual_recognition.v3.model.ClassifiedImage;
import com.ibm.watson.developer_cloud.visual_recognition.v3.model.ClassifiedImages;
import com.ibm.watson.developer_cloud.visual_recognition.v3.model.ClassifyOptions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Stream;

@Service
public class IbmWatsonVisualRecognitionTaggingService implements TaggingService {

    private final IbmWatsonVisualRecognitionProperties properties;

    @Autowired
    public IbmWatsonVisualRecognitionTaggingService(IbmWatsonVisualRecognitionProperties properties) {
        this.properties = properties;
    }

    @Override
    public String getSource() {
        return "ibm";
    }

    private ServiceCall<ClassifiedImages> createServiceCall(Resource image) throws IOException {
        final VisualRecognition service = new VisualRecognition(
                VisualRecognition.VERSION_DATE_2016_05_20, properties.getApiKey());
        final ClassifyOptions options = new ClassifyOptions.Builder()
                .imagesFile(image.getInputStream())
                .imagesFilename(image.getFilename())
                .build();
        return service.classify(options);
    }

    private Stream<IbmWatsonVisualRecognitionTag> mapResult(ClassifiedImages result) throws TaggingProcessingException {
        if (result.getImagesProcessed() != 1) {
            throw new InternalError("Requested classification of 1 image but got back " + result.getImagesProcessed() + " images");
        }
        final ClassifiedImage classifiedImage = result.getImages().get(0);
        if (classifiedImage.getError() != null) {
            throw new TaggingProcessingException(classifiedImage.getError().getDescription());
        }
        return classifiedImage.getClassifiers().stream()
                .flatMap(res -> res.getClasses().stream())
                .map(IbmWatsonVisualRecognitionTag::new);
    }

    @Override
    public Stream<IbmWatsonVisualRecognitionTag> extractTags(Resource image) throws IOException, TaggingProcessingException {
        final ClassifiedImages result = createServiceCall(image).execute();
        return mapResult(result);
    }

    @Override
    public java.util.concurrent.CompletableFuture<Stream<? extends Tag>> extractTagsAsync(Resource image) throws IOException {
        final CompletableFuture<Stream<? extends Tag>> future = new CompletableFuture<>();
        createServiceCall(image).enqueue(new ServiceCallback<ClassifiedImages>() {
            @Override
            public void onResponse(ClassifiedImages classifiedImages) {
                try {
                    future.complete(mapResult(classifiedImages));
                } catch (TaggingProcessingException e) {
                    future.completeExceptionally(e);
                }
            }

            @Override
            public void onFailure(Exception e) {
                future.completeExceptionally(e);
            }
        });
        return future;
    }

}
