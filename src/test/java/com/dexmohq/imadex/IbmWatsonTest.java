package com.dexmohq.imadex;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.ibm.watson.developer_cloud.visual_recognition.v3.VisualRecognition;
import com.ibm.watson.developer_cloud.visual_recognition.v3.model.ClassifiedImages;
import com.ibm.watson.developer_cloud.visual_recognition.v3.model.ClassifyOptions;

import java.io.IOException;

public class IbmWatsonTest {

    public static void main(String[] args) throws IOException {
        final String apiKey = ApiProperties.getKey("ibm.watson.visual-recognition.api-key");
        final VisualRecognition service = new VisualRecognition(VisualRecognition.VERSION_DATE_2016_05_20, apiKey);
        final ClassifyOptions options = new ClassifyOptions.Builder()
                .imagesFile(IbmWatsonTest.class.getResourceAsStream("/sample.jpg"))
                .imagesFilename("sample.jpg")
                .build();
        final ClassifiedImages result = service.classify(options).execute();
        final Gson gson = new GsonBuilder().setPrettyPrinting().create();
        System.out.println(gson.toJson(result));
    }

}
