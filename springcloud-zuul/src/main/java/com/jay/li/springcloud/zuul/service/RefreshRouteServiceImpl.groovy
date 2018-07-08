package com.jay.li.springcloud.zuul.service

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.cloud.netflix.zuul.RoutesRefreshedEvent
import org.springframework.cloud.netflix.zuul.filters.RouteLocator
import org.springframework.context.ApplicationEventPublisher
import org.springframework.stereotype.Service

/**
 *
 * @author jay
 * @date 2018/6/22
 */
@Service
class RefreshRouteServiceImpl implements RefreshRouteService {

    @Autowired
    RouteLocator routeLocator
    @Autowired
    ApplicationEventPublisher publisher

    @Override
    def doRefresh() {
        try {
            RoutesRefreshedEvent routesRefreshedEvent = new RoutesRefreshedEvent(routeLocator)
            publisher.publishEvent(routesRefreshedEvent)
            "success"
        } catch (Exception e) {
            "failed"
        }
    }
}
