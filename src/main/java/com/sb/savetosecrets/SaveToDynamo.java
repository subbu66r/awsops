package com.sb.savetosecrets;

import java.util.HashMap;
import java.util.Map;

import software.amazon.awssdk.services.dynamodb.DynamoDbClient;
import software.amazon.awssdk.services.dynamodb.model.AttributeValue;
import software.amazon.awssdk.services.dynamodb.model.PutItemRequest;
import software.amazon.awssdk.services.dynamodb.model.PutItemResponse;

public class SaveToDynamo {

	public void saveTokensToDynamo(String accessToken, String refreshToken) {
		System.out.println("creating dynamo client");
		// Create a DynamoDB client
		DynamoDbClient client = DynamoDbClient.create();

		// Define the table name and item attributes
		String tableName = "xerotokens";
		Map<String, AttributeValue> itemValues = new HashMap<>();
		itemValues.put("tokenid", AttributeValue.builder().n("1").build());
		itemValues.put("access_token", AttributeValue.builder().s(accessToken).build());
		itemValues.put("refresh_token", AttributeValue.builder().s(refreshToken).build());
		System.out.println("preparing db request object");
		// Create a PutItemRequest to save the item to the table
		PutItemRequest request = PutItemRequest.builder().tableName(tableName).item(itemValues).build();
		System.out.println("saving data");
		// Call the PutItem operation to save the item to the table
		PutItemResponse response = client.putItem(request);
		if (response != null) {
			System.out.println("data saved successfully " + response);
		}
		// Print the response
		System.out.println(response);

	}

}
