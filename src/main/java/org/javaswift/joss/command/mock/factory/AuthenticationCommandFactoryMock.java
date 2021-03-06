package org.javaswift.joss.command.mock.factory;

import org.apache.http.client.HttpClient;
import org.javaswift.joss.client.factory.AccountConfig;
import org.javaswift.joss.command.mock.identity.AuthenticationCommandMock;
import org.javaswift.joss.command.shared.factory.AuthenticationCommandFactory;
import org.javaswift.joss.command.shared.identity.AuthenticationCommand;
import org.javaswift.joss.swift.Swift;

public class AuthenticationCommandFactoryMock implements AuthenticationCommandFactory {

    private Swift swift;

    public AuthenticationCommandFactoryMock(Swift swift) {
        this.swift = swift;
    }

    @Override
    public AuthenticationCommand createAuthenticationCommand(HttpClient httpClient, AccountConfig config) {
        return new AuthenticationCommandMock(
            swift,
            config.getAuthUrl(),
            config.getTenantName(),
            config.getTenantId(),
            config.getUsername(),
            config.getPassword()
        );
    }

}
