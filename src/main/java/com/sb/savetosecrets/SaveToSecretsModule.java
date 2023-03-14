package com.sb.savetosecrets;

import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyResponseEvent;

import dagger.Module;
import dagger.Provides;

@Module
public class SaveToSecretsModule {

	@Provides
	public SaveToSecrets provideMyService() {
		return new SaveToSecrets();
	}

	@Provides
	public APIGatewayProxyResponseEvent provideAPIGatewayProxyResponseEvent() {
		return new APIGatewayProxyResponseEvent();
	}

}
