package com.jay.li.springcloudclient;

import com.jay.li.springcloudclient.client.CustomerClient;
import com.jay.li.springcloudclient.client.NormalClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

@SpringBootApplication
@EnableFeignClients
@EnableEurekaClient
@RestController
@RequestMapping("/client")
public class SpringcloudClientApplication {


    public static void main(String[] args) {
        SpringApplication.run(SpringcloudClientApplication.class, args);
    }

    private final CustomerClient customerClient;

    private final NormalClient normalClient;

    @Autowired
    public SpringcloudClientApplication(CustomerClient customerClient, NormalClient normalClient) {
        this.customerClient = customerClient;
        this.normalClient = normalClient;
    }

    @GetMapping("/test")
    public String test() {
        return customerClient.test();
    }

    @GetMapping("/post")
    public String post() {
        Map<String, String> map = new HashMap<>();
        map.put("id", new Random().nextInt(100) + "");
        return normalClient.doPost(map);
    }


}
