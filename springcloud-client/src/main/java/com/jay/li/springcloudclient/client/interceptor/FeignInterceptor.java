package com.jay.li.springcloudclient.client.interceptor;

import feign.RequestInterceptor;
import feign.RequestTemplate;
import org.springframework.stereotype.Component;

// 全局的拦截器所有的feign客户端rpc请求时候都会进入
@Component
public class FeignInterceptor implements RequestInterceptor {

    @Override
    public void apply(RequestTemplate requestTemplate) {
        System.out.println("进入全局拦截器");
    }
}
