package com.davifaustino.productregistrationsystem;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;

@SpringBootApplication
@OpenAPIDefinition(info = @Info(title = "Product Registration System API", version = "1", description = "Backend API"))
public class ProductRegistrationSystemApplication {

	public static void main(String[] args) {
		SpringApplication.run(ProductRegistrationSystemApplication.class, args);
	}

}
