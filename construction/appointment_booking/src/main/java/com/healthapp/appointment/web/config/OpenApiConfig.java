package com.healthapp.appointment.web.config;

import io.swagger.v3.oas.models.ExternalDocumentation;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

	@Bean
	public OpenAPI appointmentBookingOpenAPI() {
		return new OpenAPI()
			.info(new Info().title("Appointment Booking API")
				.version("v1")
				.description("APIs for patient/doctor/admin operations")
				.license(new License().name("Apache 2.0").url("https://www.apache.org/licenses/LICENSE-2.0")))
			.externalDocs(new ExternalDocumentation()
				.description("Project Docs")
				.url("https://example.com/docs"));
	}
}


