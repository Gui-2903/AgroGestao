package com.agrogestao.api.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.Contact;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("AgroGestão API - Sistema de Controle Rural")
                        .version("1.0")
                        .description("API para gestão de fazendas, estoque e documentos de produtores rurais.")
                        .contact(new Contact()
                                .name("Guilherme - Desenvolvedor")
                                .email("seu-email@exemplo.com")));
    }
}