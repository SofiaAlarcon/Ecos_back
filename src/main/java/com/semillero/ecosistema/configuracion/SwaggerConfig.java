package com.semillero.ecosistema.configuracion;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;

@Configuration
public class SwaggerConfig {
     @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Ecosistema")
                        .version("1.0")
                        .description("Esta API permite gestionar la autenticación de usuarios, así como también distintas operaciones relacionadas a proveedores y publicaciones.")
                );
    }
}
