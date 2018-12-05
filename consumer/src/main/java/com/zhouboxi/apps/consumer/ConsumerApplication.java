package com.zhouboxi.apps.consumer;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.cloud.netflix.hystrix.EnableHystrix;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;

@SpringBootApplication
@EnableDiscoveryClient
@EnableHystrix
public class ConsumerApplication {
    /**
     * @EnableDiscoveryClient 服务发现。
     * @EnableHystrix  注解表示开启断路器
     */
    public static void main(String[] args) {
        SpringApplication.run(ConsumerApplication.class, args);
    }
    /**
     * @LoadBalanced 方便我们对RestTemplate添加一个LoadBalancerClient，以实现客户端负载均衡（ribbon）
     */
    @Bean
    @LoadBalanced
    RestTemplate restTemplate(){
        return new RestTemplate();
    }

}
