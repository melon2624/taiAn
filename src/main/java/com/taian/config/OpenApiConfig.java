package com.taian.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("泰安 USDT 买卖管理系统 API")
                        .description("供 Vue 前端调用的 REST 接口文档，包含价格管理、订单增删查、统计等")
                        .version("1.0")
                        .contact(new Contact().name("泰安资产")));
    }
}
