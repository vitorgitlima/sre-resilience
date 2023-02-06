package br.com.bradescoseguros.opin.external.configuration.swagger;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
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

    @Bean
    public Docket api() {
        return new Docket(DocumentationType.SWAGGER_2)
                .select()
                .apis(RequestHandlerSelectors.basePackage("br.com.bradescoseguros.opin"))
                .paths(PathSelectors.any())
                .build();
    }

}