package com.expercise.interpreterapi.configuration;

import com.google.common.base.Predicates;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.Collections;

@Configuration
@EnableSwagger2
public class SwaggerConfiguration {

    @Value("${swagger.host.url}")
    private String host;

    @Bean
    public Docket interpreterApiDocket() {
        return new Docket(DocumentationType.SWAGGER_2)
                .host(host)
                .select()
                .apis(Predicates.not(RequestHandlerSelectors.basePackage("org.springframework")))
                .paths(PathSelectors.any())
                .build()
                .apiInfo(apiInfo());
    }

    private ApiInfo apiInfo() {
        return new ApiInfo(
                "Coding Expercise Interpreter API",
                null,
                "1.0.0",
                null,
                new Contact("expercise Labs", "https://expercise.com", "expercise@gmail.com"),
                null,
                null,
                Collections.emptyList()
        );
    }
}
