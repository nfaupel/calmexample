package com.sap.calmexample;

import com.fasterxml.jackson.annotation.JsonProperty;

public class OAuthResponse {
	
	@JsonProperty("access_token")
	private String accessToken;
	
	@JsonProperty("token_type")
	private String tokenType;
	
	@JsonProperty("expires_in")
	private int expiresIn;
	
	@JsonProperty("scope")
	private String scope;
	
	@JsonProperty("jti")
	private String jti;
	
	
	public String getAccessToken() {
		return accessToken;
	}
	
	
	public void setAccessToken(String accessToken) {
		this.accessToken = accessToken;
	}
	
	
	public String getTokenType() {
		return tokenType;
	}
	
	
	public void setTokenType(String tokenType) {
		this.tokenType = tokenType;
	}
	
	
	public int getExpiresIn() {
		return expiresIn;
	}
	
	
	public void setExpiresIn(int expiresIn) {
		this.expiresIn = expiresIn;
	}
	
	
	public String getScope() {
		return scope;
	}
	
	
	public void setScope(String scope) {
		this.scope = scope;
	}
	
	
	public String getJti() {
		return jti;
	}
	
	
	public void setJti(String jti) {
		this.jti = jti;
	}
}
