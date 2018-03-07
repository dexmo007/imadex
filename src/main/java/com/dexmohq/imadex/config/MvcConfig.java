package com.dexmohq.imadex.config;

import com.dexmohq.imadex.auth.DetailsHandlerMethodArgumentResolver;
import com.dexmohq.imadex.auth.UserIdHandlerMethodArgumentResolver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import java.util.List;

@Configuration
public class MvcConfig extends WebMvcConfigurerAdapter {

    private final UserIdHandlerMethodArgumentResolver userIdHandlerMethodArgumentResolver;
    private final DetailsHandlerMethodArgumentResolver detailsHandlerMethodArgumentResolver;

    @Autowired
    public MvcConfig(UserIdHandlerMethodArgumentResolver userIdHandlerMethodArgumentResolver,
                     DetailsHandlerMethodArgumentResolver detailsHandlerMethodArgumentResolver) {
        this.userIdHandlerMethodArgumentResolver = userIdHandlerMethodArgumentResolver;
        this.detailsHandlerMethodArgumentResolver = detailsHandlerMethodArgumentResolver;
    }

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> argumentResolvers) {
        argumentResolvers.add(userIdHandlerMethodArgumentResolver);
        argumentResolvers.add(detailsHandlerMethodArgumentResolver);
    }
}
