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
    private Set<String> authorizedGrantTypes;
    @JsonIgnore
    private Set<String> registeredRedirectUri;
    @JsonProperty
    private Set<String> authorities;
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
    public Collection<GrantedAuthority> getAuthorities() {
        return authorities.stream().map(SimpleGrantedAuthority::new).collect(Collectors.toSet());
    }

    @JsonIgnore
    @Override
    public boolean isAutoApprove(String scope) {
        return autoApproveScopes != null &&
                autoApproveScopes.stream()
                        .anyMatch(auto -> auto.equals("true") || scope.matches(auto));
    }

    public static void main(String[] args) throws IOException {
        final ObjectMapper mapper = new ObjectMapper();
        final MongoClientDetails clientDetails = new MongoClientDetails();

        clientDetails.clientId = "ci87218371";
        clientDetails.accessTokenValiditySeconds=6000;
        clientDetails.refreshTokenValiditySeconds=6000;
        clientDetails.authorities = new HashSet<>(Arrays.asList("user", "admin"));
        clientDetails.scope = new HashSet<>(Arrays.asList("read", "write"));
        clientDetails.additionalInformation = new HashMap<>();
        clientDetails.additionalInformation.put("mail", "test@mail.de");
        clientDetails.additionalInformation.put("age", 46);

        final String json = mapper.writeValueAsString(clientDetails);
        System.out.println(json);
        final MongoClientDetails read = mapper.readValue(json, MongoClientDetails.class);
        System.out.println(read);
    }

}
