package com.sap.calmexample.config;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.client.OAuth2RestOperations;
import org.springframework.security.oauth2.client.OAuth2RestTemplate;
import org.springframework.security.oauth2.client.resource.OAuth2ProtectedResourceDetails;
import org.springframework.security.oauth2.client.token.grant.client.ClientCredentialsResourceDetails;

@Configuration
public class OAuthConfiguration {
	
	@Bean
	protected OAuth2ProtectedResourceDetails resource(CalmCredentials calmCredentials) {
		ClientCredentialsResourceDetails resource = new ClientCredentialsResourceDetails();
		resource.setAccessTokenUri(calmCredentials.getUrl());
		resource.setClientId(calmCredentials.getClientId());
		resource.setClientSecret(calmCredentials.getClientSecret());
		return resource;
	}
	
	@Bean
	public OAuth2RestOperations restTemplate(OAuth2ProtectedResourceDetails oauthDetails) {
		return new OAuth2RestTemplate(oauthDetails);
	}
}
