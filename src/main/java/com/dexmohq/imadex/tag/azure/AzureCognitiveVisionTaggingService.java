package com.dexmohq.imadex.tag.azure;

import cognitivej.vision.face.scenario.ImageHolder;
import com.dexmohq.imadex.storage.StorageService;
import com.dexmohq.imadex.tag.Tag;
import com.dexmohq.imadex.tag.TaggingService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.io.ByteStreams;
import org.apache.commons.io.IOUtils;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.entity.ContentType;
import org.apache.http.impl.client.HttpClients;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.URI;
import java.nio.file.Files;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Stream;

@Service
public class AzureCognitiveVisionTaggingService implements TaggingService {

    private final StorageService storageService;
    private final AzureCognitiveProperties azureCognitiveProperties;

    @Autowired
    public AzureCognitiveVisionTaggingService(StorageService storageService, AzureCognitiveProperties azureCognitiveProperties) {
        this.storageService = storageService;
        this.azureCognitiveProperties = azureCognitiveProperties;
    }

    @Override
    public String getSource() {
        return "azure";
    }

    //    public ImageDescription describeImage(String id) throws IOException {
//        final Resource file = storageService.load(id);
//        final ComputerVisionScenario scenario = new ComputerVisionScenario(azureCognitiveProperties.getVisionSubscriptionKey());
//        return scenario.describeImage(file.getInputStream());
//    }
//
//    public OCRResult ocrImage(String id) throws IOException {
//        final Resource file = storageService.load(id);
//        final ComputerVisionScenario scenario = new ComputerVisionScenario(azureCognitiveProperties.getVisionSubscriptionKey());
//        return scenario.ocrImage(file.getURL().toExternalForm());
//    }
//
//    public List<IdentificationSet> persons(String id) throws IOException {
//        final FaceScenarios faceScenarios = new FaceScenarios(azureCognitiveProperties.getFaceSubscriptionKey(),
//                azureCognitiveProperties.getEmotionSubscriptionKey());
//        final People people = ScenarioHelper.createPeopleFromHoldingImages(candidates(), ImageNamingStrategy.DEFAULT);
//        final String group = faceScenarios.createGroupWithPeople("uihasd", people);
//        // todo need to wait for response
//        final List<IdentificationSet> idSets = faceScenarios.identifyPersonsInGroup(group, storageService.load(id).getURL().toExternalForm());
//        faceScenarios.deleteGroup(group);
//        return idSets;
//    }

    private List<ImageHolder> candidates() {
        return null;
    }

    private final ObjectMapper mapper = new ObjectMapper();

    private HttpPost createRequest(Resource image) throws IOException {
        final URI baseUri = URI.create(azureCognitiveProperties.getTagBaseUrl());
        final HttpPost request = new HttpPost(baseUri);
        request.setHeader("Content-Type", "application/octet-stream");
        request.setHeader("Ocp-Apim-Subscription-Key", azureCognitiveProperties.getVisionSubscriptionKey());
        final byte[] imageBytes = IOUtils.toByteArray(image.getInputStream());
        final ByteArrayEntity entity = new ByteArrayEntity(imageBytes, ContentType.APPLICATION_OCTET_STREAM);
        request.setEntity(entity);
        return request;
    }

    private Stream<AzureCognitiveVisionTag> execute(HttpPost request) throws IOException {
        final HttpClient httpClient = HttpClients.createDefault();
        final HttpResponse response = httpClient.execute(request);
        return mapper.readValue(response.getEntity().getContent(), AzureCognitiveVisionTagResponse.class)
                .getTags().stream();
    }

    @Override
    public Stream<AzureCognitiveVisionTag> extractTags(Resource image) throws IOException {
        final HttpPost request = createRequest(image);
        return execute(request);
    }

    @Async
    @Override
    public CompletableFuture<Stream<? extends Tag>> extractTagsAsync(Resource image) throws IOException {
        return CompletableFuture.completedFuture(extractTags(image));
    }

}
