package com.dexmohq.imadex.tag.google;

import com.google.cloud.vision.v1.*;
import com.google.protobuf.ByteString;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Files;
import java.util.Collections;
import java.util.List;

@Service
public class TaggingService {

    private static final Log log = LogFactory.getLog(TaggingService.class);

    public void describeImage(Resource image) throws IOException {
        final ImageAnnotatorClient vision = ImageAnnotatorClient.create();
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
