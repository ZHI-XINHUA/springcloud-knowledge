# 一、Zuul简介

Zuul是Netflix开源的微服务网关，它可以和Eureka、Ribbon、Hystrix等组件配合使用。

Zuul的核心是一系列的过滤器，这些过滤器可以完成以下功能。

- 身份认证与安全：识别每个资源的验证要求，并拒绝那些与要求不符的请求。
- 审查与监控：在边缘位置追踪有意义的数据和统计结果，从而带来精确的生成视图。
- 动态路由：动态地将请求路由到不同的后端集群。
- 压力测试：逐渐增加指向集群的流量，以了解性能。
- 负载分配：为每一种负载类型分配对应容量，并弃用超出限定值的请求。
- 静态响应处理：在边缘位置直接建立部分响应，从而避免其转发到内部集群。
- 多区域弹性：跨越AWS Region进行请求路由，旨在实现ELB（Elastic Load Balancing）使用的多样化，以及让系统的边缘更贴近系统的使用者。



Spring Cloud对Zuul进行了整合与增强。目前，Zuul使用默认HTTP客户端是Apache HTTP Client，也可以使用RestClient或者okhttp3.OKHttpClient.



# 二、编写Zuul微服务网关

项目：`gateway-zuul`

## 1、添加项目依赖

Zuul依赖`spring-cloud-starter-netflix-zuul`和eureka 客户端`spring-cloud-starter-netflix-eureka-client`

```xml
 <dependency>
     <groupId>org.springframework.cloud</groupId>
     <artifactId>spring-cloud-starter-netflix-zuul</artifactId>
</dependency>
<dependency>
    <groupId>org.springframework.cloud</groupId>
    <artifactId>spring-cloud-starter-netflix-eureka-client</artifactId>
</dependency>
```



## 2、在启动类上添加注解`@EnableZuulProxy`

`@EnableZuulProxy`：声明一个Zuul代理，该代理使用Ribbon来定位注册在Eureka Server中的微服务；同时还整合了Hystrix，从而实现类容错，所有经过Zuul的请求都会在Hystrix命令中执行。

```java
@SpringBootApplication
@EnableZuulProxy  
public class GatewayZuulApp
{
    public static void main( String[] args )
    {
        SpringApplication.run(GatewayZuulApp.class,args);
    }
}
```



## 3、编写application.yml配置

```yaml
spring:
  application:
    name: gateway-zuul
server:
  port: 8040

# eureka 配置
eureka:
  client:
    service-url:
      defaultZone: http://localhost:8761/eureka #Eureka Server交互地址，查询服务和注册服务都需要依赖这个地址，多个用逗号分隔。
  instance:
    prefer-ip-address: true
```

可以看到，这里没有写Zuul的配置。只是简单将Zuul注册到Eureka Server上。 

因为默认情况下，Zuul会代理所有注册到Eureka Server的微服务，并且Zuul的理由规则如下：

- http://ZUUL_HOST:ZUUL_PORT/微服务在Eureka上的serviceId/**

  访问以上地址会转发到serviceIdd对应的微服务。



## 4、启动项目测试

1、 启动注册中心：`eureka-server-ha`

2、启动服务提供者：`provider-user`

3、启动服务消费者：`consumer-movie-ribbon`

4、启动zuul：`gateway-zuul`



访问：<http://localhost:8040/consumer-movie-ribbon/movie/e98b0351-9fa9-4de7-92e9-cf5c79c663ef> 

会转发到

<http://localhost:9092/movie/e98b0351-9fa9-4de7-92e9-cf5c79c663ef> 



zuul可以使用Ribbon达到负载均衡的效果，也可以使用Hystrix容错和监控。





# 三、Zuul路由端点（待续）



# 四、路由配置详解

## 1、自定义指定微服务的访问路径

配置`zuul.routes.指定微服务的serviceId=指定路径`

```yaml
zuul:
  routes:
    consumer-movie-ribbon: /provider-user/user/**
```

consumer-movie-ribbon 微服务就会被映射到 /provider-user/user/**路径



## 2、忽略指定微服务

使用`zuul.ignored-services`配置需要忽略的服务，多个用逗号分隔

```yaml
zuul:
  ignored-services: consumer-movie-ribbon
```

上面配置则忽略consumer-movie-ribbon服务，只代理其他微服务



## 3、忽略所有微服务，只路由指定微服务（测试不通过，待进一步研究）

如果只想让Zuul代理指定的微服务，可以将`zuul.ignored-services`设置为'*'

```yaml
zuul:
  routes:
    consumer-movie-ribbon: /provider-user/user/**
  ignored-services: '*'
```

Zuul只路由consumer-movie-ribbon微服务



## 4、同时指定微服务的serviceId和对应路径

```yaml
zuul:
  routes:
    user-route:  # user-route只是给路由一个名称，可以任意起名
      service-id: consumer-movie-ribbon #微服务id
      path: /provider-user/user/**  #service-id对应的路径
```

效果和1一样



## 5、同时指定path和URL（待校验）

```yaml
zuul:
  routes:
    user-route:  # user-route只是给路由一个名称，可以任意起名
      url: http://localhost:8000/ # 指定的url
      path: /provider-user/user/**  #service-id对应的路径
```

这样就可以将`/user/**`映射到`http://localhost:8000/**`

**注意：** 使用这种方式配置的路由不会作为HystrixCommand执行，也不能使用Ribbon来负载均衡多个URL。



## 6、同时指定path和URL，并且不破坏Zuul的Hystrix、Ribbon特性































