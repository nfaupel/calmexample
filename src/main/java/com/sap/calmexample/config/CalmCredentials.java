package com.sap.calmexample.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "calmexample.uaa")
public class CalmCredentials {
	
	@Value("${calmexample.uaa.clientid}")
	private String clientId;
	
	@Value("${calmexample.uaa.clientsecret}")
	private String clientSecret;
	
	@Value("${calmexample.uaa.url}")
	private String url;
	
	@Value("${calmexample.uaa.tenantid}")
	private String tenantId;
	
	
	public String getClientId() {
		return clientId;
	}
	
	
	public String getClientSecret() {
		return clientSecret;
	}
	
	
	public String getUrl() {
		return url;
	}
	
	
	public String getTenantId() {
		return tenantId;
	}
	
	
	public void setTenantId(String tenantId) {
		this.tenantId = tenantId;
	}
}