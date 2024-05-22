package com.sap.calmexample;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Base64;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

public class CalmExampleApplication {

	public static void main(String[] args) throws IOException {
		String tenantBaseUrl = "https://dev.eu10.alm.cloud.sap/";
		
		String accessToken = getAccessToken();
		System.out.println(accessToken);
		
		RestTemplate restTemplate = restTemplate(accessToken);
		
		Map<String, String> businessProcessReq = new HashMap<>();
		businessProcessReq.put("name", "MY API Business Process" + System.currentTimeMillis());
		businessProcessReq.put("description", "MY Business Process Created via API");
		
		ResponseEntity<Map> mapResponseEntity = restTemplate.postForEntity(
				tenantBaseUrl + "/api/calm-processauthoring/v1/businessProcesses",
				new HttpEntity<>(businessProcessReq),
				Map.class
		);
		System.out.println("Business Process created: " + mapResponseEntity.getStatusCode() + " " + mapResponseEntity.getBody());
		
		
		
		Map<String, Object> solutionProcessReq = new HashMap<>();
		solutionProcessReq.put("name", "MY API Custom Solution Process");
		solutionProcessReq.put("description", "MY Solution Process Created via API");
		solutionProcessReq.put("countries", "DE,FR");
		solutionProcessReq.put("externalId", "MY_PROC1");
		solutionProcessReq.put("businessProcess", mapResponseEntity.getBody());
		
		ResponseEntity<CreateSolutionProcessResponse> solutionProcessResponseEntity = restTemplate.postForEntity(
				tenantBaseUrl + "/api/calm-processauthoring/v1/solutionProcesses",
				new HttpEntity<>(solutionProcessReq),
				CreateSolutionProcessResponse.class
		);
		System.out.println("Solution Process created " + solutionProcessResponseEntity.getStatusCode());

		
		
		ResponseEntity<GetSolutionProcessFlowResponse> solutionProcessFlows = restTemplate.getForEntity(
				tenantBaseUrl + "/api/calm-processauthoring/v1/solutionProcesses/" + solutionProcessResponseEntity.getBody().getId() + "/solutionProcessFlows",
				GetSolutionProcessFlowResponse.class
		);
		
		System.out.println("solutionProcessFlows: " + solutionProcessFlows.getStatusCode() + "\n" + solutionProcessFlows.getBody());
		
		
//		ResponseEntity<String> responseEntity = restTemplate.exchange(
//				tenantBaseUrl + "/solutionProcessFlows/" + solutionProcessFlows.getBody().getValue().get(0).getId() + "/solutionProcessFlowDiagrams/bpmn",
//				HttpMethod.HEAD,
//				new HttpEntity<>("null"),
//				String.class
//		);
		
//		System.out.println("HEAD: " + responseEntity.getStatusCode() + " " + responseEntity.getBody());
		
		Resource resource = new ClassPathResource("procure_parts.bpmn");
		byte[] bytes = Files.readAllBytes(Paths.get(resource.getURI()));

		String bpmnString = new String(bytes);
		Map<String, Object> bpmnMap = new HashMap<>();
		bpmnMap.put("bpmn", bpmnString);
		bpmnMap.put("name", "MY BPMN Diagram");
		
		HttpEntity<Map<String, Object>> entity = new HttpEntity<>(bpmnMap);
		
		ResponseEntity<String> bpmnDiagramResponse = restTemplate.postForEntity(
				tenantBaseUrl + "/solutionProcessFlows/" + solutionProcessFlows.getBody().getValue().get(0).getId() + "/solutionProcessFlowDiagrams/bpmn",
				entity,
				String.class
		);
		System.out.println("BPMN Diagram created: " + bpmnDiagramResponse.getStatusCode() + " " + bpmnDiagramResponse.getBody());
		
		
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
				
				System.out.println("Access Token scope: " + response.getScope());
				return response.getAccessToken();
				
			} else {
				System.out.println("Failed to get response, response code: " + responseCode);
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "ERROR";
	}
	
	public static RestTemplate restTemplate(String accessToken) {
		RestTemplate restTemplate = new RestTemplate();
		
		restTemplate.setInterceptors(Collections.singletonList((request, body, execution) -> {
			HttpHeaders headers = request.getHeaders();
			headers.add(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken);
			return execution.execute(request, body);
		}));
		
		return restTemplate;
	}
	
}
