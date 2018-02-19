package com.dexmohq.imadex.auth;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.provider.token.TokenEnhancer;
import org.springframework.security.oauth2.provider.token.TokenEnhancerChain;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;

import java.util.Arrays;
import java.util.Collections;

@Configuration
@EnableAuthorizationServer
public class AuthorizationServerConfig extends AuthorizationServerConfigurerAdapter {


    private final MongoClientDetailsService clientDetailsService;
    private final JwtAccessTokenConverter accessTokenConverter;
    private final TokenStore tokenStore;
    private final AuthenticationManager authenticationManager;

    @Autowired
    public AuthorizationServerConfig(MongoClientDetailsService clientDetailsService,
                                     @Qualifier("accessTokenConverter") JwtAccessTokenConverter accessTokenConverter,
                                     @Qualifier("tokenStore") TokenStore tokenStore,
                                     AuthenticationManager authenticationManager) {
        this.clientDetailsService = clientDetailsService;
        this.accessTokenConverter = accessTokenConverter;
        this.tokenStore = tokenStore;
        this.authenticationManager = authenticationManager;
    }

    @Override
    public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
        clients.withClientDetails(clientDetailsService);
    }

    @Override
    public void configure(AuthorizationServerEndpointsConfigurer endpoints) throws Exception {
        final TokenEnhancerChain enhancerChain = new TokenEnhancerChain();
        enhancerChain.setTokenEnhancers(Arrays.asList(idTokenEnhancer(), accessTokenConverter));
        endpoints.tokenStore(tokenStore)
//                .accessTokenConverter(accessTokenConverter)
                .tokenEnhancer(enhancerChain)
                .authenticationManager(authenticationManager);
    }

    @Bean
    public TokenEnhancer idTokenEnhancer() {
        return new IdTokenEnhancer();
    }
}
