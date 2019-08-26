package com.jay.li.springcloud.gateway.config;

import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;

/**
 * @author jay
 * @date 19-8-23 上午10:37
 */
@Configuration
public class GatewayConfig {

    @Bean
    @Order(-1)
    public GlobalFilter a() {
        return (exchange, chain) -> {
            return chain.filter(exchange);
        };
    }

    @Bean
    @Order(1)
    public GlobalFilter b() {
        return (exchange, chain) -> {
            return chain.filter(exchange);
        };
    }

    /**
     * 使用factory方式可以在yml中配置
     */
//    @Bean
//    public RouteLocator routeLocator(RouteLocatorBuilder builder) {
//        return builder.routes().route(predicateSpec -> predicateSpec.path("/demo-client/**")
//                .uri("lb://demo-client")
//                .filters(new RateLimitFilter(10, 1, Duration.ofMillis(1000)))
//                .id("demo-client")).build();
//    }

}
