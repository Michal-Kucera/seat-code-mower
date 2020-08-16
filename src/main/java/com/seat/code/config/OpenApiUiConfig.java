package com.seat.code.config;

import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.context.annotation.Configuration;

import springfox.documentation.oas.annotations.EnableOpenApi;

@Configuration
@ConditionalOnWebApplication
@EnableOpenApi
class OpenApiUiConfig {

}
