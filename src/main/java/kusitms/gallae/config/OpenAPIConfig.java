package kusitms.gallae.config;


import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.servers.Server;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class OpenAPIConfig {

    private final String devUrl;
    private final String prodUrl;

    public OpenAPIConfig(
            @Value("${travelmaker.openapi.dev-url}") final String devUrl,
            @Value("${travelmaker.openapi.prod-url}") final String prodUrl
    ) {
        this.devUrl = devUrl;
        this.prodUrl = prodUrl;
    }

    @Bean
    public OpenAPI openAPI() {
        final Server devServer = new Server();
        devServer.setUrl(devUrl);
        devServer.description("개발 환경 서버 URL");

        final Server prodServer = new Server();
        prodServer.setUrl(prodUrl);
        prodServer.description("운영 환경 서버 URL");

        final Info info = new Info()
                .title("TravelMaker API")
                .version("v1.0.0")
                .description("TravelMaker API");

        return new OpenAPI()
                .info(info)
                .servers(List.of(devServer, prodServer));
    }

}
