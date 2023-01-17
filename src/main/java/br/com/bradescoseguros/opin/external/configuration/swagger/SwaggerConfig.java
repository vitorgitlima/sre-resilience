package br.com.bradescoseguros.opin.external.configuration.swagger;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@Configuration
@EnableSwagger2
public class SwaggerConfig {

    @Value("${info.application.swaggerVersion}")
    private String swagerVersion;

    @Bean
    ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                .title("API Demonstração de Práticas de SRE")
                .description("API Demonstração de Práticas de SRE.")
                .license("")
                .licenseUrl("http://unlicense.org")
                .termsOfServiceUrl("")
                .version(swagerVersion)
                .build();
    }

}