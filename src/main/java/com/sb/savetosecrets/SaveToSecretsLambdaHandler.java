package com.sb.savetosecrets;

import java.util.Map;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyResponseEvent;
import com.fasterxml.jackson.databind.ObjectMapper;

public class SaveToSecretsLambdaHandler
		implements RequestHandler<APIGatewayProxyRequestEvent, APIGatewayProxyResponseEvent> {

	private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

	@Override
	public APIGatewayProxyResponseEvent handleRequest(APIGatewayProxyRequestEvent input, Context context) {
		try {
			System.out.println("inside halde request " + new ObjectMapper().writeValueAsString(input));
			// Access the request body
			String requestBody = input.getBody();
			System.out.println("request body " + requestBody);
			Map<String, String> bodyJson = OBJECT_MAPPER.readValue(requestBody, Map.class);
			// Extract the "code" string from the request body
			String code = bodyJson.get("code");
			// Do something with the input data
			//new XeroIntegration().invokeXeroForTokens(code);
			System.out.println("calling secrets class");
			new SaveToSecrets().saveToSecrets(code);
			System.out.println("call to secrets class is success");
			String output = "Received code: " + code;
			APIGatewayProxyResponseEvent response = new APIGatewayProxyResponseEvent().withStatusCode(200)
					.withBody(output);
			System.out.println("lambda processed successfully " + response.getBody());
			return response;
		} catch (Exception e) {
			APIGatewayProxyResponseEvent errorResponse = new APIGatewayProxyResponseEvent().withStatusCode(500)
					.withBody("Error processing request: " + e.getMessage());
			return errorResponse;
		}
	}

}
