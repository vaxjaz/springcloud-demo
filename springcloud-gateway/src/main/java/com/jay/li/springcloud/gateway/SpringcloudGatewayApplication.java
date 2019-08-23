package com.jay.li.springcloud.gateway;

import com.jay.li.springcloud.gateway.service.CustomerGatewayService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
@EnableDiscoveryClient
@RestController
@RequestMapping("/refresh")
public class SpringcloudGatewayApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpringcloudGatewayApplication.class, args);
    }

    @Autowired
    private CustomerGatewayService customerGatewayService;

    @GetMapping("/route")
    public String refresh() {
        customerGatewayService.notifyChange();
        return "success";
    }
}
