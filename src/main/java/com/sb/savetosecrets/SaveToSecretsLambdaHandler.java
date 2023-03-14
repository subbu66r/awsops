package com.sb.savetosecrets;

import java.util.Map;

import javax.inject.Inject;
import javax.inject.Provider;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyResponseEvent;
import com.fasterxml.jackson.databind.ObjectMapper;

public class SaveToSecretsLambdaHandler
		implements RequestHandler<APIGatewayProxyRequestEvent, APIGatewayProxyResponseEvent> {

	private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

	@Inject
	SaveToSecrets saveToSecrets;

	@Inject
	Provider<APIGatewayProxyResponseEvent> apiGatewayProxyResponseEvent;

	public SaveToSecretsLambdaHandler() {
		DaggerSaveToSecretsComponent.create().inject(this);
	}

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
			// new XeroIntegration().invokeXeroForTokens(code);
			System.out.println("calling secrets class");
			System.out.println("saveToSecrets " + saveToSecrets);
			saveToSecrets.saveToSecrets(code);
			System.out.println("call to secrets class is success");
			String output = "Received code: " + code;
			System.out.println("apiGatewayProxyResponseEvent " + apiGatewayProxyResponseEvent);
			APIGatewayProxyResponseEvent response = apiGatewayProxyResponseEvent.get().withStatusCode(200)
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