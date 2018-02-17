package com.dexmohq.imadex;

import com.dexmohq.imadex.data.UserRepository;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class ImadexApplication {

	private static final Log log = LogFactory.getLog(ImadexApplication.class);

	@Bean
	CommandLineRunner started(UserRepository userRepository) {
		return (args) -> {
//			final User user = new User();
//			user.setUserId(UUID.randomUUID().toString());
//			user.setUsername("dexmo");
//			userRepository.save(user);
//			final User user2 = new User();
//			user2.setUserId(UUID.randomUUID().toString());
//			user2.setUsername("debra");
//			userRepository.save(user2);
		};
	}

	public static void main(String[] args) {
		SpringApplication.run(ImadexApplication.class, args);
	}
}
