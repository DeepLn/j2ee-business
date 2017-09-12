package com.midea.meicloud.demoservice;

import com.midea.meicloud.entitybase.MyRestTemplate;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cloud.client.circuitbreaker.EnableCircuitBreaker;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.session.data.redis.config.annotation.web.http.EnableRedisHttpSession;
import org.springframework.web.client.RestTemplate;

/**
 * @auth: 陈佳攀
 * @Description:
 * @Date: Created in 14:09 2017-8-29
 */

@SpringBootApplication
@EnableDiscoveryClient
@EnableCircuitBreaker
@EnableRedisHttpSession
@EnableCaching
public class DemoApp extends AppBaseSwagger{

    public static void main(String[] args) throws Exception{
        new SpringApplicationBuilder(DemoApp.class).web(true).run(args);
    }

    @Bean
    @LoadBalanced
    public RestTemplate restTemplate() {
        return new MyRestTemplate();
    }
}
