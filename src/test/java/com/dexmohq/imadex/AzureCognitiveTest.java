package com.dexmohq.imadex;

import cognitivej.vision.computervision.ComputerVisionScenario;
import cognitivej.vision.computervision.ImageDescription;

import java.io.IOException;
import java.util.Properties;
import java.util.stream.Collectors;

public class AzureCognitiveTest {
    public static void main(String[] args) throws IOException {
        final Properties properties = new Properties();
        properties.load(AzureCognitiveTest.class.getResourceAsStream("/azure.properties"));
        final String apiKey = properties.getProperty("azure.cognitive.vision-subscription-key");

        final ComputerVisionScenario vision = new ComputerVisionScenario(apiKey);
        final ImageDescription imageDescription = vision.describeImage(AzureCognitiveTest.class.getResourceAsStream("/sample.jpg"));
        System.out.println("Format: " + imageDescription.metadata.format);
        final ImageDescription.Description desc = imageDescription.description;
        System.out.println("Tags: " + desc.tags.stream().collect(Collectors.joining(",")));
        System.out.println("Captions: " + desc.captions.stream().map(c -> c.text + " (" + c.confidence + ")").collect(Collectors.joining(",")));
    }
}
