package com.dexmohq.imadex;

import com.google.gson.Gson;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Properties;

public class AzureCognitiveTest {

    private static final String uriBase = "https://westeurope.api.cognitive.microsoft.com/vision/v1.0/analyze";

    private static final String tagBase = "https://westeurope.api.cognitive.microsoft.com/vision/v1.0/tag";


    public static void main(String[] args) throws IOException, URISyntaxException {
        final Properties properties = new Properties();
        properties.load(AzureCognitiveTest.class.getResourceAsStream("/tagging.properties"));
        final String key = properties.getProperty("azure.cognitive.vision-subscription-key");
        final URI uri = new URIBuilder(tagBase).build();

        final HttpPost request = new HttpPost(uri);
        request.setHeader("Content-Type","application/octet-stream");
        request.setHeader("Ocp-Apim-Subscription-Key", key);

        final HttpClient httpClient = HttpClients.createDefault();
        final ByteArrayEntity entity = new ByteArrayEntity(Files.readAllBytes(Paths.get(
                AzureCognitiveTest.class.getResource("/sample.jpg").toURI())),
                ContentType.APPLICATION_OCTET_STREAM);
        request.setEntity(entity);
        final HttpResponse response = httpClient.execute(request);
        final HttpEntity responseEntity = response.getEntity();
        System.out.println(EntityUtils.toString(responseEntity));
    }
}
