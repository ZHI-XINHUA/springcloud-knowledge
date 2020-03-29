package com.zxh.springcloud.config;

import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

/**
 * 配置类
 */
@Configuration
public class ApplicationContextConfig {

    /**
     * 创建RestTemplate 实现Web client的远程调用
     * @return
     */
    @Bean
    @LoadBalanced //负载均衡
    public RestTemplate restTemplate(){
        return new RestTemplate();
    }
}
