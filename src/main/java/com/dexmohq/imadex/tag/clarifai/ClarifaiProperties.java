package com.dexmohq.imadex.tag.clarifai;

import lombok.Getter;
import lombok.Setter;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

@Component
@PropertySource("classpath:tagging.properties")
@ConfigurationProperties(prefix = "clarifai")
@Getter
@Setter
public class ClarifaiProperties implements InitializingBean {

    private String apiKey;

    private static final Log log = LogFactory.getLog(ClarifaiProperties.class);

    @Override
    public void afterPropertiesSet() throws Exception {
        if (apiKey == null) {
            log.error("Clarifai api key not provided");
        }
    }
}
