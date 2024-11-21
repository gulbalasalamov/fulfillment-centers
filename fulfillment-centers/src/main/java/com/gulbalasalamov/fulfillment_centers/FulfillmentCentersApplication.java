package com.gulbalasalamov.fulfillment_centers;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

@EnableWebMvc
@OpenAPIDefinition

@SpringBootApplication
public class FulfillmentCentersApplication {

	public static void main(String[] args) {
		SpringApplication.run(FulfillmentCentersApplication.class, args);
	}

}
