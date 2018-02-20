package com.dexmohq.imadex.auth;

import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.token.DefaultAccessTokenConverter;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class ClaimsAwareAccessTokenConverter extends DefaultAccessTokenConverter {
    @Override
    public OAuth2Authentication extractAuthentication(Map<String, ?> claims) {
        final OAuth2Authentication auth = super.extractAuthentication(claims);
        auth.setDetails(claims);
        return auth;
    }
}
