package com.dexmohq.imadex.auth.util;

import lombok.experimental.UtilityClass;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.provider.authentication.OAuth2AuthenticationDetails;
import org.springframework.web.context.request.WebRequest;

import java.util.Map;

@UtilityClass
public class OAuthUtils {

    public static OAuth2AuthenticationDetails getDetails(WebRequest request) {
        final Authentication authentication = (Authentication) request.getUserPrincipal();
        return (OAuth2AuthenticationDetails) authentication.getDetails();
    }

    public static Map<?, ?> getDetailsMap(WebRequest request) {
        return ((Map) getDetails(request));
    }

}
