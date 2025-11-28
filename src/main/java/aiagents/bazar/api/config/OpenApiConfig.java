package aiagents.bazar.api.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.info.License;
import io.swagger.v3.oas.annotations.servers.Server;
import org.springframework.context.annotation.Configuration;

@Configuration
@OpenAPIDefinition(
        info = @Info(
                title = "Bazar API",
                version = "v1",
                description = "REST API для Bazar: категории, задания, заявки и пользователи Telegram.",
                contact = @Contact(
                        name = "Bazar",
                        url = "https://example.com"
                ),
                license = @License(
                        name = "Proprietary"
                )
        ),
        servers = {
                @Server(url = "http://localhost:8080", description = "Local")
        }
)
public class OpenApiConfig {
}


