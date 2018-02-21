package com.dexmohq.imadex.auth;

import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configurers.ResourceServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.token.DefaultTokenServices;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.store.JwtTokenStore;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

@Configuration
@EnableResourceServer
public class ResourceServerConfig extends ResourceServerConfigurerAdapter {

    private final ImadexSecurityProperties securityProperties;
    private final ClaimsAwareAccessTokenConverter claimsAwareAccessTokenConverter;

    @Autowired
    public ResourceServerConfig(ImadexSecurityProperties securityProperties,
                                ClaimsAwareAccessTokenConverter claimsAwareAccessTokenConverter) {
        this.securityProperties = securityProperties;
        this.claimsAwareAccessTokenConverter = claimsAwareAccessTokenConverter;
    }

    @Override
    public void configure(HttpSecurity http) throws Exception {
        http
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED).and()
                .authorizeRequests()
                .antMatchers("/user/**").permitAll()
                .antMatchers("/storage/**", "/tag/**").authenticated();
    }

    @Override
    public void configure(ResourceServerSecurityConfigurer resources) throws Exception {
        resources.resourceId(securityProperties.getResourceIds().getGeneral())
                .tokenServices(resourceTokenServices());
    }

    @Bean
    public TokenStore resourceTokenStore() {
        return new JwtTokenStore(resourceAccessTokenConverter());
    }

    @Bean
    public JwtAccessTokenConverter resourceAccessTokenConverter() {
        final JwtAccessTokenConverter converter = new JwtAccessTokenConverter();
        converter.setAccessTokenConverter(claimsAwareAccessTokenConverter);

        final Resource resource = new ClassPathResource("security/public.txt");
        final String publicKey;
        try {
            publicKey = IOUtils.toString(resource.getInputStream(), StandardCharsets.UTF_8);
        } catch (final IOException e) {
            throw new RuntimeException(e);
        }
        converter.setVerifierKey(publicKey);
        return converter;
    }

    @Bean
    public DefaultTokenServices resourceTokenServices() {
        final DefaultTokenServices defaultTokenServices = new DefaultTokenServices();
        defaultTokenServices.setTokenStore(resourceTokenStore());
        return defaultTokenServices;
    }

}
