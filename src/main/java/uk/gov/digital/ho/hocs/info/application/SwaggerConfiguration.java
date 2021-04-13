package uk.gov.digital.ho.hocs.info.application;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@Configuration
@EnableSwagger2
@Profile({"swagger"})
public class SwaggerConfiguration implements WebMvcConfigurer{

    @Bean
    public Docket api() {

        System.out.println("Swagger enabled in Info Service Application...");

        return new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(new ApiInfo("DECS Info Service API","Info Service Endpoints", "", "", "","",""))
                .select()
                .apis(RequestHandlerSelectors.any())
                .paths(PathSelectors.any())
                .build();
    }

}
