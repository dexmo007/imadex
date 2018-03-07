package com.dexmohq.imadex.auth;

import org.springframework.security.oauth2.common.DefaultOAuth2AccessToken;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.authentication.OAuth2AuthenticationDetails;
import org.springframework.security.oauth2.provider.token.TokenEnhancer;

import java.util.HashMap;
import java.util.Map;

public class IdTokenEnhancer implements TokenEnhancer {

    static final String USER_ID_KEY = "user_id";

    @Override
    public OAuth2AccessToken enhance(OAuth2AccessToken accessToken, OAuth2Authentication authentication) {
//        authentication.getPrincipal()
        final String userId = ((User) authentication.getUserAuthentication().getPrincipal()).getUserId();
        final HashMap<String, Object> additionalInfo = new HashMap<>();
        additionalInfo.put(USER_ID_KEY, userId);
        ((DefaultOAuth2AccessToken) accessToken).setAdditionalInformation(additionalInfo);
        return accessToken;
    }

//    @SuppressWarnings("unchecked")
//    public static String getUserId(OAuth2Authentication auth) {
//        return  (String) ((Map<String, ?>) ((OAuth2AuthenticationDetails) auth.getDetails())
//                .getDecodedDetails()).get(IdTokenEnhancer.USER_ID_KEY);
//    }

}
