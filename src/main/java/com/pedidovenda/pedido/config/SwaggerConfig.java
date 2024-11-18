package com.pedidovenda.pedido.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2WebFlux;

@Configuration
@EnableSwagger2WebFlux
public class SwaggerConfig {
    private String applicationName;

    public SwaggerConfig(@Value("${spring.application.name}") String applicationName) {
        this.applicationName = applicationName;
    }

    @Bean
    public Docket apiV1() {
        return buildDocket("v1");
    }

    private Docket buildDocket(String version) {
        return buildDocket(version, version);
    }

    private Docket buildDocket(String groupName, String version) {
        return new Docket(DocumentationType.SWAGGER_2)
                .groupName(groupName)
                .apiInfo(buildApiInfo(version, applicationName))
                .select()
                .apis(RequestHandlerSelectors.basePackage("com.pedidovenda.pedido.api." + version))
                .paths(PathSelectors.any())
                .build()
                .pathMapping("/");
    }

    private ApiInfo buildApiInfo(String version, String applicationName) {
        return new ApiInfoBuilder()
                .title(applicationName)
                .version(version)
                .build();
    }
}
