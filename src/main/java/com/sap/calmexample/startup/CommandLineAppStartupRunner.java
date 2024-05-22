package com.sap.calmexample.startup;

import com.sap.calmexample.config.CalmCredentials;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.oauth2.client.OAuth2RestOperations;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.stereotype.Component;

@Component
public class CommandLineAppStartupRunner implements CommandLineRunner {
	
	private final CalmCredentials calmCredentials;
	private final OAuth2RestOperations oAuth2RestOperations;
	
	
	public CommandLineAppStartupRunner(CalmCredentials calmCredentials, OAuth2RestOperations oAuth2RestOperations) {
		this.calmCredentials = calmCredentials;
		this.oAuth2RestOperations = oAuth2RestOperations;
	}
	private static final Logger LOG = LoggerFactory.getLogger(CommandLineAppStartupRunner.class);
	
	
	@Override
	public void run(String...args) {
		LOG.info("Start CommandLineAppStartupRunner");
		LOG.info("Calm Credentials: " + calmCredentials.getClientId() + " " + calmCredentials.getClientSecret() + " " + calmCredentials.getUrl() + " " + calmCredentials.getTenantId());
		
		OAuth2AccessToken accessToken = oAuth2RestOperations.getAccessToken();
		LOG.info("Access Token: " + accessToken.getValue());
		
		
	}
}
