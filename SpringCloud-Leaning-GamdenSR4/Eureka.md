# Eureka 

> Eureka 是Netflix开源的服务发现组件，本身是基于REST的服务。它包含Server和Client两部分。Sprinc Cloud将它集成在子项目Spring Cloud Netflix中,从而实现微服务的注册和发现。

**前提：demo  基于spring cloud Gamden：**

```xml
 <!--导入spring cloud的依赖管理-->
  <dependencyManagement>
    <dependencies>
      <dependency>
        <groupId>org.springframework.cloud</groupId>
        <artifactId>spring-cloud-dependencies</artifactId>
        <version>Gamden.SR4</version>
        <type>pom</type>
        <scope>import</scope>
      </dependency>
    </dependencies>
</dependencyManagement>
```



## 一、简单Eureka 服务

项目：`discovery-eureka`

### 1、导入eureka server 依赖

```xmk
 <dependency>
     <groupId>org.springframework.cloud</groupId>
     <artifactId>spring-cloud-starter-eureka-server</artifactId>
 </dependency>
```



### 2、开启eureka server服务

在启动类添加`@EnableEurekaServer`

```java
@SpringBootApplication
@EnableEurekaServer //开启eureka server
public class DiscoveryEurekaApp
{
    public static void main( String[] args )
    {
        SpringApplication.run(DiscoveryEurekaApp.class,args);
    }
}

```



### 3、application.yml中配置eureka

```properties
spring:
  application:
    name: discovery-eureka
server:
  port: 8761

# eureka 配置
eureka:
  client:
    register-with-eureka: false
    fetch-registry: false
    service-url:
      defaultZone: http://localhost:8761/eureka 

```

- `eureka.client.register-with-eureka`：表示十分将自己注册到Eureka Server。默认为true
- `eureka.client.fetch-registry`：表示是否从Eureka Server获取注册信息，默认为true。
- `eureka.client.service-url.defaultZone`：Eureka Server交互地址，查询服务和注册服务都需要依赖这个地址，多个用逗号分隔。



### 4、运行

DiscoveryEurekaApp#main

访问：http://localhost:8761/

