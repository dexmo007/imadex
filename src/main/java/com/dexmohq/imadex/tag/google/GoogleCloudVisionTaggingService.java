package com.dexmohq.imadex.tag.google;

import com.dexmohq.imadex.tag.Tag;
import com.dexmohq.imadex.tag.TaggingService;
import com.google.api.gax.core.FixedCredentialsProvider;
import com.google.auth.oauth2.ServiceAccountCredentials;
import com.google.cloud.vision.v1.*;
import com.google.protobuf.ByteString;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class GoogleCloudVisionTaggingService implements TaggingService {

    @Value("classpath:Imadex-1bbf90f0848d.json")
    private Resource credentialsResource;

    private ServiceAccountCredentials credentials;//todo put settings here

    @PostConstruct
    void init() {
        try (final InputStream in = credentialsResource.getInputStream()) {
            credentials = ServiceAccountCredentials.fromStream(in);
            credentialsResource = null;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static final Log log = LogFactory.getLog(GoogleCloudVisionTaggingService.class);

    @Override
    public List<Tag> extractTags(Resource image) {
        final ImageAnnotatorClient vision;
        try {
            vision = ImageAnnotatorClient.create();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        final byte[] bytes;
        try {
            bytes = Files.readAllBytes(image.getFile().toPath());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        final ByteString byteString = ByteString.copyFrom(bytes);
        final Image img = Image.newBuilder().setContent(byteString).build();
        final Feature feature = Feature.newBuilder().setType(Feature.Type.LABEL_DETECTION).build();
        final AnnotateImageRequest request = AnnotateImageRequest.newBuilder()
                .addFeatures(feature)
                .setImage(img)
                .build();
        final BatchAnnotateImagesResponse response = vision.batchAnnotateImages(Collections.singletonList(request));
        final List<AnnotateImageResponse> responsesList = response.getResponsesList();
        return responsesList.stream().filter(res -> !res.hasError())
                .flatMap(res -> res.getLabelAnnotationsList().stream())
                .map(GoogleCloudVisionTag::new)
                .collect(Collectors.toList());
    }

    public void describeImage(Resource image) throws IOException {
        final ImageAnnotatorSettings settings = ImageAnnotatorSettings.newBuilder()
                .setCredentialsProvider(FixedCredentialsProvider.create(credentials)).build();
        final ImageAnnotatorClient vision = ImageAnnotatorClient.create(settings);
        final byte[] bytes = Files.readAllBytes(image.getFile().toPath());
        final ByteString byteString = ByteString.copyFrom(bytes);
        final Image img = Image.newBuilder().setContent(byteString).build();
        final Feature feature = Feature.newBuilder().setType(Feature.Type.LABEL_DETECTION).build();
        final AnnotateImageRequest request = AnnotateImageRequest.newBuilder()
                .addFeatures(feature)
                .setImage(img)
                .build();
        final BatchAnnotateImagesResponse response = vision.batchAnnotateImages(Collections.singletonList(request));
        final List<AnnotateImageResponse> responsesList = response.getResponsesList();
        for (AnnotateImageResponse imageResponse : responsesList) {
            if (imageResponse.hasError()) {
                // todo what to do here
                log.error("Error during Google Vision Image Analysis: " + imageResponse.getError().getMessage());
                continue;
            }
            for (EntityAnnotation annotation : imageResponse.getLabelAnnotationsList()) {

                final String tag = annotation.getDescription();
                final float score = annotation.getScore();
                final float topicality = annotation.getTopicality();
                final String mid = annotation.getMid();
                final String locale = annotation.getLocale();
                final List<LocationInfo> locationsList = annotation.getLocationsList();
                final float confidence = annotation.getConfidence();
                final List<Property> properties = annotation.getPropertiesList();
//                annotation.get
//                annotation.getPropertiesList().forEach(property -> property.);
//                annotation.getAllFields().forEach(((fieldDescriptor, o) -> ));
            }
        }
    }
}
