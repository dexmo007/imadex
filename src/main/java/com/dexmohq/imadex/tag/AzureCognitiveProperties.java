package com.dexmohq.imadex.tag;

import lombok.Getter;
import lombok.Setter;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

/**
 * Load azure cognitive API keys from src/main/resources/azure.properties
 * <p>
 * <p>Note: This file is ignored by version control to keep secrets hidden.</p>
 */
@Component
@PropertySource("classpath:azure.properties")
@ConfigurationProperties(prefix = "azure.cognitive")
@Getter
@Setter
public class AzureCognitiveProperties implements InitializingBean {

    private String visionSubscriptionKey;

    private String faceSubscriptionKey;

    private String emotionSubscriptionKey;

    private static final Log log = LogFactory.getLog(AzureCognitiveProperties.class);

    @Override
    public void afterPropertiesSet() {
        //todo do we only want to log the error or do we want to fail the app startup?
        if (emotionSubscriptionKey == null) {
            log.error("Azure cognitive emotion API subscription key is not set.");
        }
        if (faceSubscriptionKey == null) {
            log.error("Azure cognitive face API subscription key is not set.");
        }
        if (visionSubscriptionKey == null) {
            log.error("Azure cognitive vision API subscription key is not set.");
        }
    }
}
