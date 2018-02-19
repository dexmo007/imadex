package com.dexmohq.imadex;

import com.dexmohq.imadex.auth.MongoClientDetails;
import com.dexmohq.imadex.auth.MongoClientDetailsRepository;
import com.dexmohq.imadex.auth.UserService;
import com.dexmohq.imadex.auth.UserRepository;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;

@SpringBootApplication
public class ImadexApplication {

    private static final Log log = LogFactory.getLog(ImadexApplication.class);

    @Bean
    CommandLineRunner started(MongoClientDetailsRepository mongoClientDetailsRepository,
                              UserRepository userRepository,
                              UserService userService) {
        return (args) -> {
            mongoClientDetailsRepository.deleteAll();
            userRepository.deleteAll();


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

            userService.registerUser("dexmo", "dexmo@gmail.com", "harry135");
            userService.registerUser("jb007", "james.bond@mi6.co.uk", "theresa");

            mongoClientDetailsRepository.save(clientDetails);
        };
    }

    public static void main(String[] args) {
        SpringApplication.run(ImadexApplication.class, args);
    }
}
