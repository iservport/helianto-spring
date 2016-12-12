package org.helianto;

import org.helianto.ingress.config.IngressConfig;
import org.helianto.message.config.MessageConfig;
import org.helianto.security.config.SecurityConfig;
import org.helianto.security.config.SocialConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@Configuration
@EnableAutoConfiguration
@Import({SecurityConfig.class, SocialConfig.class, MessageConfig.class, IngressConfig.class})
@SpringBootApplication(scanBasePackages={"org.helianto.*.service","org.helianto.*.controller"})
@EnableJpaRepositories(basePackages={"org.helianto.*.repository"})
@EntityScan(basePackages={"org.helianto.*.domain"})
@EnableSwagger2
public class Application {

    @Bean
    public Docket heliantoApi() {
        return new Docket(DocumentationType.SWAGGER_2)
                .select()
                .apis(RequestHandlerSelectors.any())
                .paths(PathSelectors.ant("/api/**"))
                .build()
                .pathMapping("/")
                ;
    }

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

}
