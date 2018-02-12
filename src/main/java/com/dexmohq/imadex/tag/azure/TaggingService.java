package com.dexmohq.imadex.tag.azure;

import cognitivej.vision.computervision.ComputerVisionScenario;
import cognitivej.vision.computervision.ImageDescription;
import cognitivej.vision.computervision.OCRResult;
import cognitivej.vision.face.scenario.*;
import com.dexmohq.imadex.storage.StorageService;
import com.dexmohq.imadex.tag.azure.AzureCognitiveProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;

@Service
public class TaggingService {

    private final StorageService storageService;
    private final AzureCognitiveProperties azureCognitiveProperties;


    @Autowired
    public TaggingService(StorageService storageService, AzureCognitiveProperties azureCognitiveProperties) {
        this.storageService = storageService;
        this.azureCognitiveProperties = azureCognitiveProperties;
    }

    public ImageDescription describeImage(String id) throws IOException {
        final Resource file = storageService.load(id);
        final ComputerVisionScenario scenario = new ComputerVisionScenario(azureCognitiveProperties.getVisionSubscriptionKey());
        return scenario.describeImage(file.getInputStream());
    }

    public OCRResult ocrImage(String id) throws IOException {
        final Resource file = storageService.load(id);
        final ComputerVisionScenario scenario = new ComputerVisionScenario(azureCognitiveProperties.getVisionSubscriptionKey());
        return scenario.ocrImage(file.getURL().toExternalForm());
    }

    public List<IdentificationSet> persons(String id) throws IOException {
        final FaceScenarios faceScenarios = new FaceScenarios(azureCognitiveProperties.getFaceSubscriptionKey(),
                azureCognitiveProperties.getEmotionSubscriptionKey());
        final People people = ScenarioHelper.createPeopleFromHoldingImages(candidates(), ImageNamingStrategy.DEFAULT);
        final String group = faceScenarios.createGroupWithPeople("uihasd", people);
        // todo need to wait for response
        final List<IdentificationSet> idSets = faceScenarios.identifyPersonsInGroup(group, storageService.load(id).getURL().toExternalForm());
        faceScenarios.deleteGroup(group);
        return idSets;
    }

    private List<ImageHolder> candidates() {
        return null;
    }

}
