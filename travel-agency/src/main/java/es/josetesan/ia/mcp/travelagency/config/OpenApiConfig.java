package es.josetesan.ia.mcp.travelagency.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.SpecVersion;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.servers.Server;
import java.util.List;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

  @Value("${spring.application.name}")
  private String applicationName;

  @Bean
  public OpenAPI customOpenAPI() {
    return new OpenAPI(SpecVersion.V31)
        .info(
            new Info()
                .title(applicationName + " API Documentation")
                .version("1.0")
                .description(
                    "REST API for managing travel packages, destinations, and customer bookings")
                .contact(
                    new Contact()
                        .name("Travel Agency Team")
                        .email("support@travelagency.com")
                        .url("https://travelagency.com"))
                .license(
                    new License()
                        .name("Apache 2.0")
                        .url("http://www.apache.org/licenses/LICENSE-2.0.html")))
        .servers(
            List.of(new Server().url("http://localhost:8080").description("Development server")));
  }
}
