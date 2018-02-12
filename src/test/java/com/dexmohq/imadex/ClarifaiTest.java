package com.dexmohq.imadex;

import clarifai2.api.ClarifaiBuilder;
import clarifai2.api.ClarifaiClient;
import clarifai2.dto.input.ClarifaiInput;
import clarifai2.dto.model.output.ClarifaiOutput;
import clarifai2.dto.prediction.Concept;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.Properties;
import java.util.stream.Collectors;

public class ClarifaiTest {
    public static void main(String[] args) throws IOException, URISyntaxException {
        final Properties properties = new Properties();
        properties.load(ClarifaiTest.class.getResourceAsStream("/azure.properties"));
        final String apiKey = properties.getProperty("clarifai.api-key");
        final ClarifaiClient clarifaiClient = new ClarifaiBuilder(apiKey)
                .buildSync();
        final byte[] imageBytes = Files.readAllBytes(Paths.get(ClarifaiTest.class.getResource("/sample.jpg").toURI()));
        final List<ClarifaiOutput<Concept>> clarifaiOutputs = clarifaiClient.getDefaultModels().generalModel()
                .predict()
                .withInputs(ClarifaiInput.forImage(imageBytes))
                .executeSync()
                .get();
        System.out.println(clarifaiOutputs.stream().map(co -> co.data().stream().map(c -> c.name() + " (" + c.value() + ")").collect(Collectors.joining(",")))
                .collect(Collectors.joining(";")));
        System.out.println("=============================");
        System.out.println(new GsonBuilder().setPrettyPrinting().create().toJson(clarifaiOutputs));
    }

}
