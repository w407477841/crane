package com.xywg.equipmentmonitor.config;

import com.google.common.collect.Lists;
import io.swagger.annotations.ApiOperation;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.ParameterBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.schema.ModelRef;
import springfox.documentation.service.*;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spi.service.contexts.SecurityContext;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.List;

//@Configuration
//@EnableSwagger2
public class SwaggerConfiguration {



    @Bean(value = "systemApi")
    public Docket systemApi() {
        ParameterBuilder parameterBuilder=new ParameterBuilder();
        List<Parameter> parameters= Lists.newArrayList();
        parameterBuilder.name("token").description("token令牌").modelRef(new ModelRef("String"))
                .parameterType("header").defaultValue("abc")
                .required(true).build();
        parameters.add(parameterBuilder.build());

        return new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(systemInfo())
                .groupName("A-系统接口")
                .select().apis(RequestHandlerSelectors.withMethodAnnotation(ApiOperation.class))
                .paths(PathSelectors.any())
                .build().globalOperationParameters(parameters)
                .securityContexts(Lists.newArrayList(securityContext(),securityContext1())).securitySchemes(Lists.<SecurityScheme>newArrayList(apiKey(),apiKey()));
    }

    @Bean(value = "businessApi")
    public Docket businessApi() {
        return new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(businessInfo())
                .groupName("业务接口")
                .select().apis(RequestHandlerSelectors.withMethodAnnotation(ApiOperation.class))
                .paths(PathSelectors.any())
                .build().securityContexts(Lists.newArrayList(securityContext(),securityContext1())).securitySchemes(Lists.<SecurityScheme>newArrayList(apiKey(),apiKey()));
    }

    private ApiInfo businessInfo(){
        return new ApiInfoBuilder()
                .title("业务一接口")
                .description("业务一接口描述")
                .contact("王长伟:18551824688")
                .version("1.0")
                .build();
    }

    private ApiInfo systemInfo() {
        return new ApiInfoBuilder()
                .title("系统接口")
                .description("业务模块描述")
                .contact("王长伟:18551824688")
                .version("1.0")
                .build();
    }

    private ApiKey apiKey() {
        return new ApiKey("BearerToken", "Authorization", "header");
    }

    private SecurityContext securityContext() {
        return SecurityContext.builder()
                .securityReferences(defaultAuth())
                .forPaths(PathSelectors.regex("/.*"))
                .build();
    }
    private SecurityContext securityContext1() {
        return SecurityContext.builder()
                .securityReferences(defaultAuth1())
                .forPaths(PathSelectors.regex("/.*"))
                .build();
    }

    List<SecurityReference> defaultAuth() {
        AuthorizationScope authorizationScope = new AuthorizationScope("global", "accessEverything");
        AuthorizationScope[] authorizationScopes = new AuthorizationScope[1];
        authorizationScopes[0] = authorizationScope;
        return Lists.newArrayList(new SecurityReference("BearerToken", authorizationScopes));
    }
    List<SecurityReference> defaultAuth1() {
        AuthorizationScope authorizationScope = new AuthorizationScope("global", "accessEverything");
        AuthorizationScope[] authorizationScopes = new AuthorizationScope[1];
        authorizationScopes[0] = authorizationScope;
        return Lists.newArrayList(new SecurityReference("BearerToken1", authorizationScopes));
    }

}