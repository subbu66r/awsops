package com.sb.savetosecrets;

import javax.inject.Provider;
import javax.inject.Singleton;

import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyResponseEvent;

import dagger.Component;

@Singleton
@Component(modules = { SaveToSecretsModule.class })
public interface SaveToSecretsComponent {
	public SaveToSecrets getSaveToSecrets();

	public Provider<APIGatewayProxyResponseEvent> getAPIGatewayProxyResponseEvent();

	void inject(SaveToSecretsLambdaHandler handler);
}
