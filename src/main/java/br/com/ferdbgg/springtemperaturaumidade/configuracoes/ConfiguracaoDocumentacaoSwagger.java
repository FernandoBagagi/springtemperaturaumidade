package br.com.ferdbgg.springtemperaturaumidade.configuracoes;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.security.SecurityScheme;

@Configuration
public class ConfiguracaoDocumentacaoSwagger {

    public static final String BEARER_KEY = "bearer-key";

    @Bean
    public OpenAPI customOpenAPI() {

        final var components = new Components()
                .addSecuritySchemes(BEARER_KEY, getSecurityScheme());

        return new OpenAPI()
                .components(components)
                .info(getInfo());

    }

    private SecurityScheme getSecurityScheme() {

        return new SecurityScheme()
                .type(SecurityScheme.Type.HTTP)
                .scheme("bearer")
                .bearerFormat("JWT");

    }

    private Info getInfo() {

        return new Info()
                .title("Spring API Temperatura e Umidade")
                .description(
                        "Um projeto que lê dados de um sensor de temperatura e umidade e disponibiliza através de um websocket")
                .contact(getContact())
                .license(getLicense());

    }

    private Contact getContact() {
        return new Contact()
                .name("Time Backend")
                .email("backend@teste.spring");
    }

    private License getLicense() {
        return new License()
                .name("Apache 2.0")
                .url("http://springtemperaturaumidade/api/licenca");
    }

}
