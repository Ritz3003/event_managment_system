package com.ritu.eventplatform;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableAsync
//EnableAsync annotation is used to enable asynchronous processing in a Spring Boot application. 
//It allows methods annotated with @Async to run in a separate thread,
//enabling non-blocking execution and improving performance for tasks that can be executed concurrently.
public class EventPlatformApplication {

	public static void main(String[] args) {
		SpringApplication.run(EventPlatformApplication.class, args);
	}

}
