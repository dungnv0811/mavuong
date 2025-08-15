package com.healthapp.appointment.web.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.ExternalDocumentation;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

	private static final String BEARER_KEY_SECURITY_SCHEME = "bearer-key";

	@Bean
	public OpenAPI appointmentBookingOpenAPI() {
		return new OpenAPI()
			.addSecurityItem(new SecurityRequirement().addList(BEARER_KEY_SECURITY_SCHEME))
			.components(new Components()
				.addSecuritySchemes(BEARER_KEY_SECURITY_SCHEME,
					new SecurityScheme()
						.name(BEARER_KEY_SECURITY_SCHEME)
						.type(SecurityScheme.Type.HTTP)
						.scheme("bearer")
						.bearerFormat("JWT")
						.description("JWT Authorization header using the Bearer scheme. " +
									"Enter 'Bearer' [space] and then your token in the text input below. " +
									"Example: 'Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...'")
				)
			)
			.info(new Info().title("Appointment Booking API")
				.version("v1.0")
				.description("APIs for patient/doctor/admin operations with JWT Authentication. " +
							"Use the 'Authorize' button to enter your JWT token.")
				.license(new License().name("Apache 2.0").url("https://www.apache.org/licenses/LICENSE-2.0")))
			.externalDocs(new ExternalDocumentation()
				.description("Project Docs")
				.url("https://example.com/docs"));
	}
}


