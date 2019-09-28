package com.zxh.conf;

import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

/**
 * @ClassName WebSecurityConfig
 * @Description 默认情况下，当Spring Security位于类路径上时，它将要求在每次向应用程序发送请求时都发送一个有效的CSRF令牌。
 * Eureka客户端通常不会拥有一个有效的跨站点请求伪造令牌(CSRF)，您需要禁用/ Eureka /**端点的这个要求
 * @Author xh.zhi
 * @Date 2019-9-26 10:47
 * @Version 1.0
 **/
@EnableWebSecurity
public class WebSecurityConfig  extends WebSecurityConfigurerAdapter {

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        //忽略eureka注册请求
        http.csrf().ignoringAntMatchers("/eureka/**");
        super.configure(http);
    }
}
