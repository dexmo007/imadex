package com.dexmohq.imadex.auth;

import org.springframework.security.oauth2.common.DefaultOAuth2AccessToken;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.token.TokenEnhancer;

import java.util.HashMap;

public class IdTokenEnhancer implements TokenEnhancer {//todo extract extra claims: http://www.baeldung.com/spring-security-oauth-jwt
    @Override
    public OAuth2AccessToken enhance(OAuth2AccessToken accessToken, OAuth2Authentication authentication) {
//        authentication.getPrincipal()
        final String userId = ((User) authentication.getUserAuthentication().getPrincipal()).getUserId();
        final HashMap<String, Object> additionalInfo = new HashMap<>();
        additionalInfo.put("userId", userId);
        ((DefaultOAuth2AccessToken) accessToken).setAdditionalInformation(additionalInfo);
        return accessToken;
    }
}
