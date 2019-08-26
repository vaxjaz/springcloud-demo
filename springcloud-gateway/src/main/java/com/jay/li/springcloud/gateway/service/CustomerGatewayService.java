package com.jay.li.springcloud.gateway.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.event.RefreshRoutesEvent;
import org.springframework.cloud.gateway.filter.FilterDefinition;
import org.springframework.cloud.gateway.handler.predicate.PredicateDefinition;
import org.springframework.cloud.gateway.route.RouteDefinition;
import org.springframework.cloud.gateway.route.RouteDefinitionWriter;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.ApplicationEventPublisherAware;
import org.springframework.stereotype.Service;
import org.springframework.web.util.UriComponentsBuilder;
import reactor.core.publisher.Mono;

import java.net.URI;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author jay
 * @date 19-8-23 下午2:03
 */
@Service
public class CustomerGatewayService implements ApplicationEventPublisherAware {


    @Autowired
    private RouteDefinitionWriter routeDefinitionWriter;

    private ApplicationEventPublisher publisher;


    @Override
    public void setApplicationEventPublisher(ApplicationEventPublisher applicationEventPublisher) {
        this.publisher = applicationEventPublisher;
    }

    public void notifyChange() {
        //todo 自定义路由实现,自由发挥这里随便写死的
        RouteDefinition definition = new RouteDefinition();
        PredicateDefinition predicate = new PredicateDefinition();
        Map<String, String> predicateParams = new HashMap<>(8);
        definition.setId("jdRoute");
        predicate.setName("Path");
        predicateParams.put("pattern", "/jd/**");
        predicate.setArgs(predicateParams);
        definition.setPredicates(Arrays.asList(predicate));
        URI uri = UriComponentsBuilder.fromHttpUrl("http://www.jd.com").build().toUri();
        definition.setUri(uri);
        List<FilterDefinition> filters = definition.getFilters();
        FilterDefinition filterDefinition = new FilterDefinition();
        filterDefinition.setName("RateLimitFilterFactory");
        HashMap<String, String> argMap = new HashMap<>();
        argMap.put("capacity","10");
        argMap.put("refillTokens","1");
        argMap.put("refillDuration","1");
        filterDefinition.setArgs(argMap);
        filters.add(filterDefinition);
        routeDefinitionWriter.save(Mono.just(definition)).subscribe();
        publisher.publishEvent(new RefreshRoutesEvent(this));
    }


}
