package com.jay.li.springcloudclient.client;


import com.alibaba.fastjson.JSONObject;
import com.jay.li.springcloudclient.SpringcloudClientApplication;
import feign.Contract;
import feign.RequestInterceptor;
import feign.RequestTemplate;
import feign.codec.Encoder;
import feign.form.FormEncoder;
import feign.httpclient.ApacheHttpClient;
import feign.hystrix.FallbackFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.ObjectFactory;
import org.springframework.boot.autoconfigure.http.HttpMessageConverters;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.cloud.openfeign.support.SpringEncoder;
import org.springframework.context.annotation.Bean;
import org.springframework.http.converter.FormHttpMessageConverter;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.Enumeration;
import java.util.Map;

/**
 * 　name 参数指定调用某个服务名
 * 　fallbackFactory　熔断时候进入,能拿到具体的异常信息
 * 　configuration 针对具体feign　client做定制化配置　此处示例为曾加一个拦截器
 * 　如果自定义了配置文件,那么会导致无法发送Post请求（G版本变化，之前F版本是可以的）,
 * 　原因是feign其实是就是一个httpClient的封装(feign.Feign可以在项目启动的时候debug查看如何装配的),
 *   默认使用jdk　URLConnection,本项目使用apache httpclient实现了替换.
 * 　httpclient　最重要的就是实现Decoder and Encoder,所以需要自定义编解码
 * feign 还包括了
 * Decoder：ResponseEntityDecoder
 * Encoder：SpringEncoder
 * Logger:Slf4jLogger
 * Contract:SpringMvcContract
 * Feign.Builder:
 * Client:默认Ribbon实现负载均衡
 * feign 默认集成了熔断详见配置文件
 */
@FeignClient(name = "demo-server", fallbackFactory = CustomerClient.TestFallBack.class, configuration = CustomerClient.TestConfig.class)
public interface CustomerClient {


    @GetMapping("/server/test")
    String test();

    @Component
    @Slf4j
    class TestFallBack implements FallbackFactory<CustomerClient> {
        @Override
        public CustomerClient create(Throwable throwable) {
            return new CustomerClient() {
                @Override
                public String test() {
                    log.error("进入熔断");
                    throwable.printStackTrace();
                    return "进入熔断";
                }

            };
        }
    }

    /**
     * 拦截器为了传递request不确定的content内容
     */
    class TestConfig {

        @Bean
        public RequestInterceptor requestInterceptor() {
            return new RequestInterceptor() {
                @Override
                public void apply(RequestTemplate template) {
                    ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder
                            .getRequestAttributes();
                    HttpServletRequest request = attributes.getRequest();
                    Enumeration<String> headerNames = request.getHeaderNames();
                    if (headerNames != null) {
                        while (headerNames.hasMoreElements()) {
                            String name = headerNames.nextElement();
                            String values = request.getHeader(name);
                            template.header(name, values);
                        }
                    }
                    String paramString = getParamString(request);
                    template.body(paramString);
                }

                private String getParamString(HttpServletRequest request) {
                    String param = null;
                    try {
                        BufferedReader streamReader = new BufferedReader(new InputStreamReader(request.getInputStream(), "UTF-8"));
                        StringBuilder responseStrBuilder = new StringBuilder();
                        String inputStr;
                        while ((inputStr = streamReader.readLine()) != null) {
                            responseStrBuilder.append(inputStr);
                        }
                        JSONObject jsonObject = JSONObject.parseObject(responseStrBuilder.toString());
                        if (jsonObject != null) {
                            param = jsonObject.toJSONString();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    return param;
                }
            };
        }

    }

}
