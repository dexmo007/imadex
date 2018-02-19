package com.dexmohq.imadex.auth;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configurers.ResourceServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.token.ResourceServerTokenServices;

@Configuration
@EnableResourceServer
public class ResourceServerConfig extends ResourceServerConfigurerAdapter {

    private final ResourceServerTokenServices tokenServices;
    private final ImadexSecurityProperties securityProperties;

    @Autowired
    public ResourceServerConfig(ResourceServerTokenServices tokenServices, ImadexSecurityProperties securityProperties) {
        this.tokenServices = tokenServices;
        this.securityProperties = securityProperties;
    }

    @Override
    public void configure(ResourceServerSecurityConfigurer resources) throws Exception {
        resources.resourceId(securityProperties.getResourceIds().getGeneral())
                .tokenServices(tokenServices);
    }

    @Override
    public void configure(HttpSecurity http) throws Exception {
        http
                .requestMatchers().and()
                .authorizeRequests()
                .antMatchers("/user/**").permitAll()
                .antMatchers("/storage/**", "/tag/**").authenticated();
    }
}