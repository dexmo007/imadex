package com.dexmohq.imadex.auth;

import com.dexmohq.imadex.auth.stereotype.UserId;
import com.dexmohq.imadex.auth.util.OAuthUtils;
import org.springframework.core.MethodParameter;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.provider.authentication.OAuth2AuthenticationDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebArgumentResolver;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import java.util.Map;

/**
 * Resolves user id parameter by extracted the user id from the token details
 *
 * @author Henrik Drefs
 * @see UserId
 */
@Component
public class UserIdHandlerMethodArgumentResolver implements HandlerMethodArgumentResolver {

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.hasParameterAnnotation(UserId.class)
                && parameter.getParameterType().isAssignableFrom(String.class);
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer,
                                  NativeWebRequest webRequest, WebDataBinderFactory binderFactory) {
        if (supportsParameter(parameter)) {
            return ((Map) (OAuthUtils.getDetails(webRequest))
                    .getDecodedDetails()).get(IdTokenEnhancer.USER_ID_KEY);
        }
        return WebArgumentResolver.UNRESOLVED;
    }

}
