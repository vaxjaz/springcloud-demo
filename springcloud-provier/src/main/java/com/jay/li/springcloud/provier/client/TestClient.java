package com.jay.li.springcloud.provier.client;

import com.alibaba.fastjson.JSONObject;
import feign.RequestInterceptor;
import feign.RequestTemplate;
import feign.hystrix.FallbackFactory;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.Enumeration;

@FeignClient(name = "xxxx",fallbackFactory = TestClient.TestFallBack.class,configuration = TestClient.TestConfig.class)
public interface TestClient {


    @PostMapping("/xx/xx")
    String test();

    @Component
    class TestFallBack implements FallbackFactory<TestClient>{
        @Override
        public TestClient create(Throwable throwable) {
            return () -> null;
        }
    }
    /**
     * 拦截器为了传递request不确定的content内容
     */
    class TestConfig{
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
