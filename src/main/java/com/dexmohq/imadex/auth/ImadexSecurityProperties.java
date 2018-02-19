package com.dexmohq.imadex.auth;

import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "imadex.security")
@Getter
@Setter
public class ImadexSecurityProperties implements InitializingBean {

    private PasswordEncoder passwordEncoder;

    private Jwt jwt;

    private ResourceIds resourceIds;

    @Override
    public void afterPropertiesSet() throws Exception {
        System.out.println();
    }

    @Getter
    @Setter
    public static class PasswordEncoder {
        private String storePassword;
        private String keyPassword;
    }

    @Getter
    @Setter
    public static class Jwt {
        private String signingKey;
    }

    @Getter
    @Setter
    public static class ResourceIds {
        private String general;
    }

}
