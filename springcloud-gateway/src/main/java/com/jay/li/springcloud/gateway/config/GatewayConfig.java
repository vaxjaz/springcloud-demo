package com.jay.li.springcloud.gateway.config;

import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.cloud.gateway.route.RouteLocator;
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
            System.out.println(exchange.getRequest().getRemoteAddress().getHostName());
            return chain.filter(exchange);
        };
    }

    @Bean
    @Order(1)
    public GlobalFilter b() {
        return (exchange, chain) -> {
            System.out.println(exchange.getRequest().getRemoteAddress().getHostName());
            return chain.filter(exchange);
        };
    }

}
