package com.sb.savetosecrets.test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.HashMap;
import java.util.Map;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyResponseEvent;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sb.savetosecrets.SaveToSecrets;
import com.sb.savetosecrets.SaveToSecretsLambdaHandler;

@ExtendWith(MockitoExtension.class)
public class SaveToSecretsLambdaHandlerTest {

	@InjectMocks
	SaveToSecretsLambdaHandler handler;

	@Mock
	SaveToSecrets saveToSecrets;

	@Mock
	APIGatewayProxyResponseEvent event;

	@Mock
	Context context;
	private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

	@Test
	void test() {
		try {
			// Mock the input event
			APIGatewayProxyRequestEvent inputEvent = new APIGatewayProxyRequestEvent();
			Map<String, String> bodyMap = new HashMap<>();
			bodyMap.put("code", "3452345");
			inputEvent.setBody(OBJECT_MAPPER.writeValueAsString(bodyMap));
			when(saveToSecrets.saveToSecrets(anyString())).thenReturn("testVersionId");
			APIGatewayProxyResponseEvent res = handler.handleRequest(inputEvent, context);
			// Verify the response
			verify(saveToSecrets).saveToSecrets(eq("3452345")); // Verify that SaveToSecrets was called with the
																		// correct argument
			assertEquals("200", String.valueOf(res.getStatusCode())); // Verify the status code
			assertEquals("Received code: 3452345", res.getBody()); // Verify the response body
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

}
