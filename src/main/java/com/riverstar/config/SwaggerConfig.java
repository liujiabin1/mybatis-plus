package com.riverstar.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * Author:  Hardy
 * Date:    2018/7/18 20:05
 * Description: Rest Api 自动生成工具 http://{domain}/swagger-ui.html
 **/
@Configuration
@EnableSwagger2
@ConditionalOnExpression("'${profile-env}'=='dev'")
public class SwaggerConfig {
    @Bean
    public Docket createRestApi(@Value("${swagger.domain}") String domain,
                                @Value("${swagger.api-ver}") String version,
                                @Value("${swagger.name}") String name) {
        return new Docket(DocumentationType.SWAGGER_2)
                .host(domain)
                .apiInfo(apiInfo(version, name))
                .useDefaultResponseMessages(false)
                .select()
                .apis(RequestHandlerSelectors.basePackage("com.zrb.controller"))
                .paths(PathSelectors.any())
                .build();
    }

    private ApiInfo apiInfo(String version, String name) {
        return new ApiInfoBuilder()
                .title(Character.toUpperCase(name.charAt(0)) + name.substring(1) + " Rest Api Doc")
                .version(version)
                .build();
    }
}