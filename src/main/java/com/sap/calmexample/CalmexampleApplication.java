package com.sap.calmexample;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

import com.fasterxml.jackson.databind.ObjectMapper;

public class CalmexampleApplication {

	public static void main(String[] args) {

		String tenantId = "0391180a-7352-4c9e-ba94-e00cba807d2b";
		
		String accessToken = getAccessToken();
		
		System.out.println(accessToken);
	}
	
	
	private static String getAccessToken() {
		String clientId = "sb-CALMAPI!b484816|sapcloudalm!b162106";
		String clientSecret = "958e6258-23a2-4969-97b3-25acdb19977b$ny8CLia3Jo4qkB2NxxU8fl6WCGIKJvSOw5L44vaDfao=";
		String authenticationUrl = "https://calm-dev-eu10-004-relcallstablefeatures-team-signavio.authentication.eu10.hana.ondemand.com" + "/oauth/token";
		
		try {
			URL url = new URL(authenticationUrl);
			String encoding = Base64.getEncoder().encodeToString((clientId + ":" + clientSecret).getBytes(
					StandardCharsets.UTF_8));
			
			HttpURLConnection connection = (HttpURLConnection) url.openConnection();
			connection.setRequestMethod("POST");
			connection.setDoOutput(true);
			connection.setRequestProperty("Authorization", "Basic " + encoding);
			connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded;charset=UTF-8");
			
			// Grant type client_credentials is for server-to-server authentication
			String body = "grant_type=client_credentials";
			try(OutputStream os = connection.getOutputStream()) {
				byte[] input = body.getBytes(StandardCharsets.UTF_8);
				os.write(input, 0, input.length);
			}
			
			int responseCode = connection.getResponseCode();
			if (responseCode == HttpURLConnection.HTTP_OK) {
				BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
				
				ObjectMapper mapper = new ObjectMapper();
				OAuthResponse response = mapper.readValue(in, OAuthResponse.class);
				return response.getAccessToken();
				
			} else {
				System.out.println("Failed to get response, response code: " + responseCode);
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "ERROR";
	}
	
}
