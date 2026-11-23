package it.sara.pair.common.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI openAPI(Properties props){
        return new OpenAPI().info(
            new Info()
                .title(props.title())
                .description(props.description())
                .version(props.version())
        );
    }
}
