package com.jay.li.springcloud.gateway.config;

import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;

import java.util.List;

/**
 * @author jay
 * @date 19-8-23 下午3:51
 */
public class RateLimitFactory extends AbstractGatewayFilterFactory {

    @Override
    public GatewayFilter apply(Object config) {
        return null;
    }

    @Override
    public List<String> shortcutFieldOrder() {
        return null;
    }
}
