package com.dexmohq.imadex.auth;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.provider.ClientDetails;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

@Document(collection = "client_details")
@Data
@JsonInclude(JsonInclude.Include.NON_DEFAULT)
@JsonIgnoreProperties(ignoreUnknown = true)
public class MongoClientDetails implements ClientDetails {
    @Id
    @JsonIgnore
    private String clientId;
    @JsonIgnore
    private Set<String> resourceIds;
    @JsonIgnore
    private String clientSecret;
    private Set<String> scope;
    @JsonIgnore
    private Set<String> authorizedGrantTypes = Collections.emptySet();
    @JsonIgnore
    private Set<String> registeredRedirectUri;
    @JsonProperty
    private Set<Authority> authorities = Collections.emptySet();
    @JsonIgnore
    private Integer accessTokenValiditySeconds;
    @JsonIgnore
    private Integer refreshTokenValiditySeconds;
    @JsonIgnore
    private Set<String> autoApproveScopes;
    private Map<String, Object> additionalInformation;

    @JsonIgnore
    @Override
    public boolean isSecretRequired() {
        return clientSecret != null;
    }

    @JsonIgnore
    @Override
    public boolean isScoped() {
        return scope != null && !scope.isEmpty();
    }

    @JsonIgnore
    @Override
    public Set<GrantedAuthority> getAuthorities() {
        return (Set) authorities;
    }

    @JsonIgnore
    @Override
    public boolean isAutoApprove(String scope) {
        return autoApproveScopes != null &&
                autoApproveScopes.stream()
                        .anyMatch(auto -> auto.equals("true") || scope.matches(auto));
    }

}
