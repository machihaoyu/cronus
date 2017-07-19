package com.fjs.cronus;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.netflix.feign.EnableFeignClients;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.web.client.RestTemplate;


@SpringBootApplication
@EnableDiscoveryClient
@EnableResourceServer
@EnableFeignClients
@ComponentScan("com.fjs")
public class CronusApplication {

	@Bean
	RestTemplate restTemplate() {
		return new RestTemplate();
	}


	public static void main(String[] args) {

		SpringApplication.run(CronusApplication.class, args);

	}

}
