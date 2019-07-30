package com.jay.li.springcloudservice;


import lombok.Data;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.UUID;

@SpringBootApplication
@EnableFeignClients
@EnableEurekaClient
@RestController
@RequestMapping("/server")
public class SpringcloudServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpringcloudServiceApplication.class, args);
    }

    @GetMapping("/test")
    public String test() {
        return UUID.randomUUID().toString();
    }

    @PostMapping("/post")
    public String post(@RequestBody Demo demo) {
        return demo.getId();
    }

    @Data
    public static class Demo {

        private String id;

    }

}
