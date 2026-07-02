package com.ritu.eventplatform.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;

@Configuration
public class OpenApiConfig {
	
	// this is needed to customize the OpenAPI documentation for the Event Registration Platform API.
	 @Bean
	    public OpenAPI eventPlatformApi() {

	        return new OpenAPI()
	                .info(new Info()
	                        .title("Event Registration Platform API")
	                        .version("1.0")
	                        .description("Spring Boot Event Platform"));
	    }

}
