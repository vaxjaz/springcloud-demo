package com.jay.li.springcloud.gateway.filter;

import io.github.bucket4j.Bandwidth;
import io.github.bucket4j.Bucket;
import io.github.bucket4j.Bucket4j;
import io.github.bucket4j.Refill;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.core.Ordered;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author jay
 * @date 19-8-26 下午1:59
 * 这种方式是直接实现,目前好像不能在yml中配置解析
 */
@Slf4j
@Data
@AllArgsConstructor
@NoArgsConstructor
@Component
public class RateLimitFilter implements GatewayFilter, Ordered {


    private RateLimitFilterFactory.Config config;

    private static final Map<String, Bucket> CACHE = new ConcurrentHashMap<>();


    private Bucket createBucket() {
        Refill greedy = Refill.greedy(config.getRefillTokens(), Duration.ofSeconds(config.getRefillDuration()));
        Bandwidth limit = Bandwidth.classic(config.getCapacity(), greedy);
        return Bucket4j.builder().addLimit(limit).build();
    }

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        return Mono.just(exchange.getRequest().getRemoteAddress().getAddress().getHostAddress())
                .map(s -> {
                    Bucket bucket = CACHE.computeIfAbsent(s, k -> createBucket());
                    log.info("IP: " + s + "，TokenBucket Available Tokens: " + bucket.getAvailableTokens());
                    return bucket;
                })
                .flatMap(bucket -> {
                    if (bucket.tryConsume(1)) {
                        return chain.filter(exchange);
                    }
                    exchange.getResponse().setStatusCode(HttpStatus.BAD_GATEWAY);
                    return exchange.getResponse().setComplete();
                });
    }

    @Override
    public int getOrder() {
        return -1000;
    }
}
