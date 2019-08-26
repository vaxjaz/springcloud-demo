package com.jay.li.springclouds.erver;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;

@EnableEurekaServer
@SpringBootApplication
public class SpringcloudServerRegisteredApplication {

	public static void main(String[] args) {
		SpringApplication.run(SpringcloudServerRegisteredApplication.class, args);
	}
}
