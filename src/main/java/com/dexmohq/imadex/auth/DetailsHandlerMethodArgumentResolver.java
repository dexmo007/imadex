package com.dexmohq.imadex.auth;

import com.dexmohq.imadex.auth.stereotype.Details;
import com.dexmohq.imadex.auth.util.OAuthUtils;
import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebArgumentResolver;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

@Component
public class DetailsHandlerMethodArgumentResolver implements HandlerMethodArgumentResolver {

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.hasParameterAnnotation(Details.class);
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer,
                                  NativeWebRequest webRequest, WebDataBinderFactory binderFactory) {
        if (supportsParameter(parameter)) {
            final Details details = parameter.getParameterAnnotation(Details.class);
            return OAuthUtils.getDetailsMap(webRequest).get(details.value());
        }
        return WebArgumentResolver.UNRESOLVED;
    }
}
