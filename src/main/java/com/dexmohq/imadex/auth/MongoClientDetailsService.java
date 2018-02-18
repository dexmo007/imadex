package com.dexmohq.imadex.auth;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.provider.ClientDetails;
import org.springframework.security.oauth2.provider.ClientDetailsService;
import org.springframework.security.oauth2.provider.ClientRegistrationException;
import org.springframework.security.oauth2.provider.NoSuchClientException;
import org.springframework.stereotype.Service;

@Service
public class MongoClientDetailsService implements ClientDetailsService {

    private final MongoClientDetailsRepository clientDetailsRepository;

    @Autowired
    public MongoClientDetailsService(MongoClientDetailsRepository clientDetailsRepository) {
        this.clientDetailsRepository = clientDetailsRepository;
    }

    @Override
    public ClientDetails loadClientByClientId(String clientId) throws ClientRegistrationException {
        final MongoClientDetails clientDetails = clientDetailsRepository.findOne(clientId);
        if (clientDetails == null) {
            throw new NoSuchClientException("No client with id " + clientId);
        }
        return clientDetails;
    }
}
