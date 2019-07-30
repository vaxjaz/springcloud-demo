package com.jay.li.springcloudclient.client;

import feign.hystrix.FallbackFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.Map;

/**
 * spring cloud F 之前的版本可以定义N个以上的name相同的客户端，发现G版本不能构建相同name的feign客户端了
 *
 */
@FeignClient(name = "xxxx-server", fallbackFactory = NormalClient.NormalFallBack.class)
public interface NormalClient {

    @PostMapping("/server/post")
    String doPost(Map<String, String> map);


    @Component
    @Slf4j
    class NormalFallBack implements FallbackFactory<NormalClient> {
        @Override
        public NormalClient create(Throwable throwable) {
            log.error("进入熔断 e={}", throwable.getMessage());
            return map -> "熔断返回";
        }
    }
}
