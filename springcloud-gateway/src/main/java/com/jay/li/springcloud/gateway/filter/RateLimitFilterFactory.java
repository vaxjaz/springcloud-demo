package com.jay.li.springcloud.gateway.filter;

import io.github.bucket4j.Bandwidth;
import io.github.bucket4j.Bucket;
import io.github.bucket4j.Bucket4j;
import io.github.bucket4j.Refill;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author jay
 * @date 19-8-26 下午4:19
 * 使用factory的方式创建filter是可以在yml中配置的
 */
@Component
@Slf4j
public class RateLimitFilterFactory extends AbstractGatewayFilterFactory<RateLimitFilterFactory.Config> {

    private static final String CAPACITY = "capacity";
    private static final String REFILL_TOKENS = "refillTokens";
    private static final String REFILL_DURATION = "refillDuration";

    private static final Map<String, Bucket> CACHE = new ConcurrentHashMap<>();

    public RateLimitFilterFactory() {
        super(Config.class);
    }

    @Override
    public List<String> shortcutFieldOrder() {
        return Arrays.asList(CAPACITY, REFILL_TOKENS, REFILL_DURATION);
    }

    @Override
    public GatewayFilter apply(Config config) {
        return (exchange, chain) -> Mono.just(exchange.getRequest().getRemoteAddress().getAddress().getHostAddress())
                .map(s -> {
                    Bucket bucket = CACHE.computeIfAbsent(s, k -> createBucket(config));
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

    private Bucket createBucket(Config config) {
        Refill greedy = Refill.greedy(config.getRefillTokens(), Duration.ofSeconds(config.getRefillDuration()));
        Bandwidth limit = Bandwidth.classic(config.getCapacity(), greedy);
        return Bucket4j.builder().addLimit(limit).build();
    }

    /**
     * 对应yml中的args参数
     */
    @Data
    public static class Config {

        private int capacity;

        private int refillTokens;

        private int refillDuration;

    }

}
