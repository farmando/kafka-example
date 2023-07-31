package com.fabiano.kafkaflex.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {
  @Bean
  public OpenAPI configOpenApi() {
    return new OpenAPI().info(new Info().title("Kafka example API")
        .description("Kafka API producer for learning concerns").version("1.0.0"));
  }
}
