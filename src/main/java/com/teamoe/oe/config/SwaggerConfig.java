package com.teamoe.oe.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@OpenAPIDefinition(
    info=@Info(
        title = "오늘의 이슈",
        description = "오늘의 이슈 명세",
        version="v1"
    )
)
@RequiredArgsConstructor
@Configuration
public class SwaggerConfig {

  @Bean
  public GroupedOpenApi oeApi() {
    String[] paths = {"/v1/**"};

    return GroupedOpenApi
        .builder()
        .group("oe")
        .pathsToMatch(paths)
        .build();
  }
}
