package com.learn.spring.springbootauthenticationwithjwt.config;

import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

@Configuration
@EnableSwagger2
public class SwaggerConfig {
    private static final Contact DEFAULT_CONTACT = new Contact("Ibad Nurhamim", "https://github.com/nurhamim96", "nurhamimibad96@gmail.com");
    private static final ApiInfo DEFAULT_API_INFO = new ApiInfo("Initial Spring JWT", "initial project", "1", "", DEFAULT_CONTACT, "", "", new ArrayList<>());
    private static final Set<String> DEFAULT_PRODUCES_AND_CONSUMES = new HashSet<>(Arrays.asList("application/json", "application/xml"));

    public Docket api() {
        return new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(DEFAULT_API_INFO)
                .produces(DEFAULT_PRODUCES_AND_CONSUMES)
                .consumes(DEFAULT_PRODUCES_AND_CONSUMES)
                .select()
                .apis(RequestHandlerSelectors.any())
                .paths(PathSelectors.any())
                .build();
    }
}
