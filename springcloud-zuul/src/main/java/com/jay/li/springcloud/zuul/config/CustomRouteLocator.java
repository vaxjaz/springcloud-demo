package com.jay.li.springcloud.zuul.config;

import com.jay.li.springcloud.zuul.util.GroovyUitls;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.netflix.zuul.filters.RefreshableRouteLocator;
import org.springframework.cloud.netflix.zuul.filters.SimpleRouteLocator;
import org.springframework.cloud.netflix.zuul.filters.ZuulProperties;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * @author jay
 * @date 2018/6/22
 */
public class CustomRouteLocator extends SimpleRouteLocator implements RefreshableRouteLocator {

    public final static Logger logger = LoggerFactory.getLogger(CustomRouteLocator.class);

    private ZuulProperties properties;

    public CustomRouteLocator(String servletPath, ZuulProperties properties) {
        super(servletPath, properties);
        this.properties = properties;
        logger.info("servletPath:{}", servletPath);
    }

    //父类已经提供了这个方法，这里写出来只是为了说明这一个方法很重要！！！
//    @Override
//    protected void doRefresh() {
//        super.doRefresh();
//    }

    @Override
    public void refresh() {
        doRefresh();
    }

    @Override
    protected Map<String, ZuulProperties.ZuulRoute> locateRoutes() {
        LinkedHashMap<String, ZuulProperties.ZuulRoute> routesMap = new LinkedHashMap<>();
        //从application.yml中加载路由信息
        routesMap.putAll(super.locateRoutes());
        //从自定义文件夹中加载groovy脚本添加路由信息
        routesMap.putAll(locateRoutesFromGroovy());
        //优化一下配置
        LinkedHashMap<String, ZuulProperties.ZuulRoute> values = new LinkedHashMap<>();
        for (Map.Entry<String, ZuulProperties.ZuulRoute> entry : routesMap.entrySet()) {
            String path = entry.getKey();
            // Prepend with slash if not already present.
            if (!path.startsWith("/")) {
                path = "/" + path;
            }
            if (StringUtils.hasText(this.properties.getPrefix())) {
                path = this.properties.getPrefix() + path;
                if (!path.startsWith("/")) {
                    path = "/" + path;
                }
            }
            values.put(path, entry.getValue());
        }
        return values;
    }

    private Map<String, ZuulProperties.ZuulRoute> locateRoutesFromGroovy() {
        Map<String, ZuulProperties.ZuulRoute> routes = new LinkedHashMap<>();
        List<ZuulProperties.ZuulRoute> results = null;
        try {
            results = GroovyUitls.getRoutesByScriptEngine();
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException("groovy文件路径异常！" + e.getMessage());
        }
        for (ZuulProperties.ZuulRoute result : results) {
            // 必须传入path和id，id建议自增,groovy文件建议使用serviceId作为路由，url可选
            if (StringUtils.isEmpty(result.getPath()) || StringUtils.isEmpty(result.getId())) {
                continue;
            }
            routes.put(result.getPath(), result);
        }
        return routes;
    }
}
