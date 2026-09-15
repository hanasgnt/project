package com.bootcamp.project.configuration;

import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;

@Configuration
public class OpenApiConfiguration {

    @Bean
    public OpenAPI springOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Sistem Transaksi dan Stok Barang API")
                        .description("API untuk manajemen category, supplier, product, transaksi, dan reporting")
                        .version("v1.0.0"));
    }

    @Bean
    public GroupedOpenApi apiGroupA() {
        return GroupedOpenApi.builder()
                .group("Bootcamp API")
                .pathsToMatch("/**")
                .packagesToScan("com.bootcamp.project.controller")
                .build();
    }
}