package com.cs203.core.config;


import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.servers.Server;
import io.swagger.v3.oas.annotations.tags.Tag;

@OpenAPIDefinition(
        info = @Info(
                title = "TARIFF",
                version = "1.0.0",
                description = "Tariff Calculation System for UBS, by G2T3"
        ),
        servers = {
                @Server(url = "http://localhost:8080", description = "Local server")
        },
        tags = {
                @Tag(name = "Tariff Rate", description = "APIs for managing tariff rates for the application"),
                @Tag(name = "Country", description = "Country operations"),
                @Tag(name = "User", description = "Operations about user"),
                @Tag(name = "Authentication", description = "Authentication operations")
        }
)
public class OpenApiConfig {
}
