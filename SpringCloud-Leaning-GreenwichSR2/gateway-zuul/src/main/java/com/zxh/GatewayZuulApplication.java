package com.zxh;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.zuul.EnableZuulProxy;


@SpringBootApplication
@EnableZuulProxy // 声明一个Zuul代理，该代理使用Ribbon来定位注册在Eureka Server中的微服务；同时还整合了Hystrix，从而实现类容错，
//所有经过Zuul的请求都会在Hystrix命令中执行
public class GatewayZuulApplication {

    public static void main(String[] args) {
        SpringApplication.run(GatewayZuulApplication.class, args);
    }

}
