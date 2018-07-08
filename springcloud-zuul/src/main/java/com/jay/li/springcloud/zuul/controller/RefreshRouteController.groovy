package com.jay.li.springcloud.zuul.controller

import com.jay.li.springcloud.zuul.service.RefreshRouteService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

/**
 *
 * @author jay
 * @date 2018/6/22
 */
@RestController
@RequestMapping("/refresh")
class RefreshRouteController {

    @Autowired
    private RefreshRouteService refreshRouteService;

    /**
     * 主动刷新路由规则
     * @return
     */
    @GetMapping("/route")
    def refresh() {
        refreshRouteService.doRefresh()
    }
}
