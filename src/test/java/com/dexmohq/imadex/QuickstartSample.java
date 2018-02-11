package com.dexmohq.imadex;// Imports the Google Cloud client library

import com.google.api.gax.core.FixedCredentialsProvider;
import com.google.api.gax.core.GoogleCredentialsProvider;
import com.google.auth.oauth2.ComputeEngineCredentials;
import com.google.auth.oauth2.ServiceAccountCredentials;
import com.google.cloud.vision.v1.*;
import com.google.cloud.vision.v1.Feature.Type;
import com.google.protobuf.ByteString;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class QuickstartSample {
    public static void main(String... args) throws Exception {

        // Instantiates a client
        final ServiceAccountCredentials credentials = ServiceAccountCredentials.fromStream(QuickstartSample.class.getResourceAsStream("/Imadex-1bbf90f0848d.json"));
        final ImageAnnotatorSettings settings = ImageAnnotatorSettings.newBuilder().setCredentialsProvider(FixedCredentialsProvider.create(credentials)).build();
        try (ImageAnnotatorClient vision = ImageAnnotatorClient.create(settings)) {

            // The path to the image file to annotate
            String fileName = "/P1220092.JPG";
            // Reads the image file into memory
            final ByteString imgBytes = ByteString.readFrom(QuickstartSample.class.getResourceAsStream(fileName));
//            Path path = Paths.get(fileName);
//            byte[] data = Files.readAllBytes(path);
//            ByteString imgBytes = ByteString.copyFrom(data);

            // Builds the image annotation request
            List<AnnotateImageRequest> requests = new ArrayList<>();
            Image img = Image.newBuilder().setContent(imgBytes).build();
            Feature feat = Feature.newBuilder().setType(Type.LABEL_DETECTION).build();
            AnnotateImageRequest request = AnnotateImageRequest.newBuilder()
                    .addFeatures(feat)
                    .setImage(img)
                    .build();
            requests.add(request);
            // Performs label detection on the image file
            BatchAnnotateImagesResponse response = vision.batchAnnotateImages(requests);
            List<AnnotateImageResponse> responses = response.getResponsesList();

            for (AnnotateImageResponse res : responses) {
                if (res.hasError()) {
                    System.out.printf("Error: %s\n", res.getError().getMessage());
                    return;
                }

                for (EntityAnnotation annotation : res.getLabelAnnotationsList()) {

                    final String mid = annotation.getMid();
                    final String tag = annotation.getDescription();
                    final float score = annotation.getScore();
                    final float topicality = annotation.getTopicality();
                    final String locale = annotation.getLocale();
                    final List<LocationInfo> locationsList = annotation.getLocationsList();
                    final String locations = locationsList.stream()
                            .map(li -> li.getLatLng().getLatitude() + "," + li.getLatLng().getLongitude())
                            .collect(Collectors.joining("|"));
                    final float confidence = annotation.getConfidence();
                    final List<Property> properties = annotation.getPropertiesList();

                    final String propertiesString = properties.stream().map(p -> p.getName() + ":" + p.getValue()).collect(Collectors.joining(","));

                    System.out.println("mid=" + mid + ";tag=" + tag + ";score=" + score + ";topicality=" + topicality +
                            ";locations=" + locations + ";locale=" + locale + ";confidence=" + confidence + ";properties=" + propertiesString);
                }
            }
        }
    }
}