package com.dexmohq.imadex.tag.ibm;

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
@ConfigurationProperties(prefix = "ibm.watson.visual-recognition")
@Getter
@Setter
public class IbmWatsonVisualRecognitionProperties implements InitializingBean {

    private String apiKey;

    private static final Log log = LogFactory.getLog(IbmWatsonVisualRecognitionProperties.class);

    @Override
    public void afterPropertiesSet() throws Exception {
        if (apiKey == null) {
            log.error("IBM Watson Visual Recognition API key not provided");
        }
    }
}
