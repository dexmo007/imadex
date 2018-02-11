package com.dexmohq.imadex;

import com.dexmohq.imadex.tag.AzureCognitiveProperties;
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
	CommandLineRunner started(AzureCognitiveProperties acp) {
		return (args) -> {
			log.info(acp.getEmotionSubscriptionKey());
			log.info(acp.getFaceSubscriptionKey());
			log.info(acp.getVisionSubscriptionKey());
		};
	}

	public static void main(String[] args) {
		SpringApplication.run(ImadexApplication.class, args);
	}
}
