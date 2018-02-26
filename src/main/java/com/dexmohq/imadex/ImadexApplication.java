package com.dexmohq.imadex;

import com.dexmohq.imadex.auth.*;
import com.dexmohq.imadex.data.*;
import lombok.extern.apachecommons.CommonsLog;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.util.*;

@CommonsLog
@SpringBootApplication
public class ImadexApplication {

    @Bean
    CommandLineRunner started(MongoClientDetailsRepository mongoClientDetailsRepository,
                              UserRepository userRepository,
                              UserService userService,
                              TaggedImageRepository tagRepository) {
        return (args) -> {
            mongoClientDetailsRepository.deleteAll();

            final MongoClientDetails clientDetails = new MongoClientDetails();
            clientDetails.setClientId("ci87218371");
            clientDetails.setAccessTokenValiditySeconds(6000);
            clientDetails.setRefreshTokenValiditySeconds(6000);
            clientDetails.setRegisteredRedirectUri(Collections.singleton("general"));
            clientDetails.setScope(new HashSet<>(Arrays.asList("read", "write")));
            final HashMap<String, Object> additionalInformation = new HashMap<>();
            additionalInformation.put("mail", "test@mail.de");
            additionalInformation.put("age", 46);
            clientDetails.setAdditionalInformation(additionalInformation);
            mongoClientDetailsRepository.save(clientDetails);

        };
    }

    public static void main(String[] args) {
        SpringApplication.run(ImadexApplication.class, args);
    }
}
