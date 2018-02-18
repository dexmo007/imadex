package com.dexmohq.imadex;

import com.dexmohq.imadex.auth.MongoClientDetails;
import com.dexmohq.imadex.auth.MongoClientDetailsRepository;
import com.dexmohq.imadex.data.UserRepository;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;

@SpringBootApplication
public class ImadexApplication {

	private static final Log log = LogFactory.getLog(ImadexApplication.class);

	@Bean
	CommandLineRunner started(MongoClientDetailsRepository mongoClientDetailsRepository) {
		return (args) -> {
			final MongoClientDetails clientDetails = new MongoClientDetails();

			final String clientId = "ci87218371";
			clientDetails.setClientId(clientId);
			clientDetails.setAccessTokenValiditySeconds(6000);
			clientDetails.setRefreshTokenValiditySeconds(6000);
			clientDetails.setAuthorities(new HashSet<>(Arrays.asList("user", "admin")));
			clientDetails.setScope(new HashSet<>(Arrays.asList("read", "write")));
			final HashMap<String, Object> additionalInformation = new HashMap<>();
			additionalInformation.put("mail", "test@mail.de");
			additionalInformation.put("age", 46);
			clientDetails.setAdditionalInformation(additionalInformation);

			mongoClientDetailsRepository.save(clientDetails);

			final MongoClientDetails roundTrip = mongoClientDetailsRepository.findOne(clientId);
			log.info(roundTrip);
		};
	}

	public static void main(String[] args) {
		SpringApplication.run(ImadexApplication.class, args);
	}
}
