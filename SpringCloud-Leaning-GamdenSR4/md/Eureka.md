# Eureka:微服务注册与发现 

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



## 二、将微服务注册到Eureka Server上

项目：`consumer-movie`、`provider-user`

### 1、导入依赖

```xml
<dependency>
    <groupId>org.springframework.cloud</groupId>
    <artifactId>spring-cloud-starter-eureka-server</artifactId>
</dependency>
```

注意：不同版本的cloud引入依赖会有所区别



### 2、application.yml配置Eureka

```yaml
eureka:
  client:
    register-with-eureka: true
    fetch-registry: true
    service-url:
      defaultZone: http://127.0.0.1:8761/eureka/,http://127.0.0.1:8762/eureka/
  instance:
    prefer-ip-address: true #将自己的ip地址注册到Eureka服务中
    #ip-address: 127.0.0.1
    instance-id: ${spring.application.name}:${server.port}
```

- `eureka.instance.prefer-ip-address`：true 表示将自己的IP注册到Eureka Server
- `eureka.instance.instance-id`：注册实例名称



### 3、在启动类添加@EnableDiscoveryClient注解，声明这是一个EurekaClient

```java
@SpringBootApplication
@EnableDiscoveryClient //或 @EnableEurekaClient
public class ProviderUserApp
{
    public static void main( String[] args )
    {
        SpringApplication.run(ProviderUserApp.class,args);
    }
}
```

`@EnableDiscoveryClient` 和 `@EnableEurekaClient`都可以声明是EurekaClient，区别在于：

- `@EnableDiscoveryClient`：在spring cloud中服务发现组件有很多选择，例如`Zookeeper`、`Consul`等。`@EnableDiscoveryClient`为各种组件提供了支持。 是`spring-cloud-commons`项目的注解。
- `@EnableEurekaClient`：只支持Eureka的Client.是`sping-cloud-netflix`项目中的注解。



启动后，Eureka服务上注册了名称为` ${spring.application.name}:${server.port}`的服务。



## 三、Eureka Server 高可用

> 在分布式应用中，单节点Eureka Server并不适合线上生成环境。Eureka Client会定时连接Eureka Server，获取服务注册表中的信息并缓存在本地。微服务咋消费远程API时总是使用本地缓存中的数据，即使Eureka Server发生宕机，也不会影响到服务之间的调用。
>
> 但如果Eureka Server宕机时，某些微服务也出现了不可用的情况，Eureka Client中的缓存若不更新，就可能会影响到微服务的调用，甚至影响到整个应用系统的高可用性。
>
> 因此，在生成环境中，通常部署一个高可用的Eureka Server集群。



demo项目：`eureka-server-ha`



和一种的步骤一样，需要修改的是`application.yml`，配置多个eureka server服务

```yaml
spring:
  application:
    name: eureka-server 

---
spring:
     profiles: eureka-8761
server:
  port: 8761
eureka:
  client:
    register-with-eureka: true
    fetch-registry: true
    service-url:
      defaultZone: http://127.0.0.1:8762/eureka/ #将自己注册到8762中
  instance:
    prefer-ip-address: true #将自己的ip地址注册到Eureka服务中
    ip-address: 127.0.0.1
    instance-id: ${spring.application.name}:${server.port}
---
spring:
  profiles: eureka-8762
server:
  port: 8762
eureka:
  client:
    register-with-eureka: true
    fetch-registry: true
    service-url:
      defaultZone: http://127.0.0.1:8761/eureka/ #将自己注册到8761中
  instance:
    prefer-ip-address: true #将自己的ip地址注册到Eureka服务中
    ip-address: 127.0.0.1
    instance-id: ${spring.application.name}:${server.port}
```

- 使用连接符`---在`配置文件中配置了两个eureka server，启动时设置运行参数启动
- `register-with-eureka`和`fetch-registry`都设置true，将节点注册到其它节点上，在eureka server上拉取节点信息。实现节点之间的同步，高可用。
- 启动端口8761时设置jvm参数：`-Dspring.profiles.active=eureka-8761`； 8762设置为`-Dspring.profiles.active=eureka-8762`



## 四、Eureka Server添加用户认证

前面实例中，Eureka Server是运行匿名访问的。现在构建一个需要登录才能访问的Eureka Server。

项目：`discovery-eureka-auth`

在一的例子中修改：

### 1、添加引入security依赖

```xml
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-security</artifactId>
</dependency>
```

### 2、application.yml文件中添加security配置

```yaml
security:
  basic:
    enabled: true  #开启基于Http basic的认证
  user:
    name: admin       #配置登录账户
    password: 123456  #配置登录密码
```



启动，访问http://localhost:8761/

![](.\img\1.png)



### 3、微服务注册到需要认证的Eureka Server

和上面的例子类似，区别在于Eureka Server地址添加用户和密码

```yaml
eureka:
  client:
    service-url:
      defaultZone: http://admin:123456@127.0.0.1:8761/eureka/
```

`http://127.0.0.1:8761/eureka/` 修改为 `http://admin:123456@127.0.0.1:8761/eureka/`





## 五、Eureka元数据

Eureka的元数据有两种：标准元数据和自定义元数据

- 标准元数据：指的是主机名、IP地址、端口号、状态页和健康检查等信息，这些信息都会被发布在服务注册表中，用于服务之间的调用。
- 自定义元数据：可以使用`eureka.instance.metadata-map`配置



demo：`provider-user-my-metadata`、`consumer-movie-understanding-metadata`



### 1、在user微服务添加自定义元数据

application.yml:

```yaml
eureka:
  client:
    register-with-eureka: true
    fetch-registry: true
    service-url:
      defaultZone: http://127.0.0.1:8761/eureka/,http://127.0.0.1:8762/eureka/
  instance:
    prefer-ip-address: true #将自己的ip地址注册到Eureka服务中
    #ip-address: 127.0.0.1
    instance-id: ${spring.application.name}:${server.port}
    metadata-map:  #自定义元数据，key/value形式
      name: metadata
      msg: metadata-map test
```

- metadata-map是可以key/value集合

  ```yaml
  metadata-map:  #自定义元数据，key/value形式
        name: metadata
        msg: metadata-map test
  ```

  

### 在movie微服务中访问

```java
@RestController
@RequestMapping("/movie")
public class MovieController {

    @Autowired
    private DiscoveryClient discoveryClient;


    /**
     * 查询provider-user-my-metadata服务的信息
     * @return
     */
    @GetMapping("/user-instance")
    public List<ServiceInstance> showInfo(){
        return discoveryClient.getInstances("provider-user-my-metadata");
    }
}
```



访问：http://localhost:9092/movie/user-instance

![](./img/2.png)



## 六、自我保护模式

![](./img/3.png)



默认情况下，如果Eureka Server在一定时间内没有接收到某个微服务实例的心态，Eureka Server将会注销该实例（默认90s）。但是当网络分区故障发生时，微服务与Eureka Server之间无法正常通信（这时候行为可能变得危险，因为微服务本身其实是健康的，此时不应该注销这个微服务）。

Eureka通过“自我保护模式”来解决这个问题：当Eureka Server节点在短时间内丢失过多客户端时（可能发送了网络分区故障），那么这个节点就会进入自我保护模式。一旦进入该模式，Eureka Server就会保护服务注册表中的信息，不在删除服务注册表中的数据（也就是不会注销任何微服务）。当网络故障恢复后，该Eureka Server节点会自动退出自我保护模式。

禁用自我保护模式的方法,`enable-self-preservation=false`，如下：

```yaml
eureka:
    server:
        enable-self-preservation: false
```



ps:使用自我保护模式，让Eureka集群更加健壮、稳定。生产环境建议不禁用。





## 七、Eureka健康检查



































