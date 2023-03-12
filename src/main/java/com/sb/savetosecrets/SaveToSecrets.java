package com.sb.savetosecrets;

import java.util.HashMap;
import java.util.Map;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.secretsmanager.SecretsManagerClient;
import software.amazon.awssdk.services.secretsmanager.model.PutSecretValueRequest;
import software.amazon.awssdk.services.secretsmanager.model.PutSecretValueResponse;

public class SaveToSecrets {

	public String saveToSecrets(String code) {

		String secretName = "nonprod/xero/authcode";
		String secretValue = null;

		try {
			System.out.println("inside save to secrets");
			Map<String, String> secretValues = new HashMap<>();
			ObjectMapper mapper = new ObjectMapper();
			System.out.println("invoking Xero from secrets class");
			String xeroTokenResponse = new XeroIntegration().invokeXeroForTokens(code);
			System.out.println("got the response and parsing");
			JsonNode node = mapper.readTree(xeroTokenResponse);
			String accessToken = node.get("access_token").asText();
			String refreshToken = node.get("refresh_token").asText();
			secretValues.put("XERO_ACCESS_TOKEN", accessToken);
			secretValues.put("XERO_REFRESH_TOKEN", refreshToken);
			// Create Secrets Manager client
			System.out.println("creating secret client");
			SecretsManagerClient client = SecretsManagerClient.create();
			secretValue = new ObjectMapper().writeValueAsString(secretValues);
			System.out.println("secrets value " + secretValue);
			PutSecretValueRequest putSecretValueRequest = PutSecretValueRequest.builder().secretId(secretName)
					.secretString(secretValue).build();
			System.out.println("saving secrets ");
			PutSecretValueResponse putSecretValueResult = client.putSecretValue(putSecretValueRequest);
			System.out.println("The secret is saved with version ID: " + putSecretValueResult.versionId());
			new SaveToDynamo().saveTokensToDynamo(accessToken, refreshToken);
			return putSecretValueResult.versionId();
		} catch (Exception e) {
			e.printStackTrace();
		}

		return null;
	}

}
