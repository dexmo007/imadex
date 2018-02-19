package com.dexmohq.imadex.auth;

import com.dexmohq.imadex.auth.crypto.Aes256BCryptSha512PasswordEncoder;
import com.dexmohq.imadex.auth.crypto.JceksKeyProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class PasswordEncoderConfig {

    private final ImadexSecurityProperties imadexSecurityProperties;

    @Autowired
    public PasswordEncoderConfig(ImadexSecurityProperties imadexSecurityProperties) {
        this.imadexSecurityProperties = imadexSecurityProperties;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new Aes256BCryptSha512PasswordEncoder(
                JceksKeyProvider.builder()
                        .storePassword(imadexSecurityProperties.getPasswordEncoder().getStorePassword())
                        .keyPassword(imadexSecurityProperties.getPasswordEncoder().getKeyPassword())
        );
    }

}
