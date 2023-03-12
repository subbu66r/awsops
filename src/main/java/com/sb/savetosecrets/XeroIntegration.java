package com.sb.savetosecrets;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

public class XeroIntegration {

	public String invokeXeroForTokens(String code) {

		try {
			System.out.println("inside invokeXeroForTokens " + code);
			String endpoint = "https://identity.xero.com/connect/token";
			String clientID = "<CLIENT_ID>";
			String clientSecret = "<CLIENT_SECRET>";
			String authorizationHeader = "Basic " + Base64.getEncoder()
					.encodeToString((clientID + ":" + clientSecret).getBytes(StandardCharsets.UTF_8));
			String grantType = "authorization_code";
			String redirectUri = "http://localhost:3000/api";

			URL url = new URL(endpoint);
			HttpURLConnection con = (HttpURLConnection) url.openConnection();
			con.setRequestMethod("POST");
			con.setRequestProperty("Authorization", authorizationHeader);
			con.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");

			String formParams = "grant_type=" + grantType + "&code=" + code + "&redirect_uri=" + redirectUri;
			byte[] postData = formParams.getBytes(StandardCharsets.UTF_8);
			con.setDoOutput(true);
			try (OutputStream outputStream = con.getOutputStream()) {
				outputStream.write(postData, 0, postData.length);
			}

			int responseCode = con.getResponseCode();
			System.out.println("responseCode " + responseCode);
			if (responseCode == HttpURLConnection.HTTP_OK) {
				BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
				String inputLine;
				StringBuilder response = new StringBuilder();
				while ((inputLine = in.readLine()) != null) {
					response.append(inputLine);
				}
				in.close();
				System.out.println(response.toString());
				//saveToDynamoDb(response.toString());
				return response.toString();
			} else {
				System.out.println("Request failed with HTTP error code: " + responseCode);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return null;
	}

	private void saveToDynamoDb(String xeroTokenResponse) {
		try {
			System.out.println("parsing json");
			ObjectMapper mapper = new ObjectMapper();
			JsonNode node = mapper.readTree(xeroTokenResponse);
			String accessToken = node.get("access_token").asText();
			String refreshToken = node.get("refresh_token").asText();
			System.out.println("access token " + accessToken);
			System.out.println("refresh token " + refreshToken);
			new SaveToDynamo().saveTokensToDynamo(accessToken, refreshToken);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
