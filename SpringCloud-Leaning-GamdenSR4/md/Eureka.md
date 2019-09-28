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

- `eureka.client.register-with-eureka`：表示将自己注册到Eureka Server。默认为true
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



**问题：如果Eureka高可用注册中心registered-replicas没有分布式注册中心**

![](img/2019-9-12-1.png)



可能的原因有如下：

1. `eureka.client.serviceUrl.defaultZone`配置项的地址，不能使用localhost，要使用service-center-1之类的域名，通过host映射到127.0.0.1；

2. `spring.application.name`或`eureka.instance.appname`必须一致；

3. 相互注册要开启：

   ```yaml
   eureka.client.register-with-eureka=true
   eureka.client.fetch-registry=true
   ```

原文链接：https://blog.csdn.net/u012138272/article/details/81412689



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



**application.yml.** 

```yaml
eureka:
  client:
    healthcheck:
      enabled: true
```





## 八、服务治理机制

### （1）服务提供者

#### 服务注册

>  “服务提供者”在启动的时候会通过发送REST请求的方式将自己注册到Eureka Server上，同时带上了自身服务的一些元数据信息。Eureka Server接受到这个REST请求之后，**将元数据信息存储在一个双层Map中，其中第一层key是服务名，第二层key是具体服务的实例名。**

- `eureka.client.register-with-eureka`：默认为true，true时 启动注册到注册中心



#### 服务同步

> 注册中心之间相互注册服务，当服务提供者发送注册请求到一个注册中心时，它会将该请求转发给集群中相连的其他注册中心，从而实现注册中心之间的服务同步。这样服务提供者的服务信息旧可以通过任意一台注册中心获取到。



#### 服务续约

> 注册完服务后，服务提供者会维护一个心跳用来持续告诉Eureka Server：“我还活着”，已防止Eureka Server的“剔除任务”将该服务实例从服务列表中排除出去--------称为服务续约

有关服务续约的两个重要属性：

- `eureka.instance.lease-renewal-interval-in-seconds`：默认30秒，用于定义服务续约任务的调用间隔时间
- `eureka.instance.lease-expiration-duration-in-seconds`：默认90秒，用于定义服务失效的时间



### （2）服务消费者

#### 获取服务

> 消费者会发送一个REST请求给服务注册中心来获取注册的服务清单。为了性能考虑，Eureka Server会维护一份只读的服务清单来返回给客户端，同时该缓存清单会每隔30秒更新一次

- `eureka.client.fetch-registry`： 拉取注册中心服务缓存清单。默认为true，获取服务是服务消费的基础，所以必须确保这个参数为true
- `eureka.client.registry-fetch-interval-seconds`：默认30秒，更新缓存清单时间



####  服务调用

> 服务消费者在获取服务清单后，通过服务名可以获得具体服务的实例名和改实例的元数据信息。根据这些信息，客户端可以根据自己的需要决定具体调用那个实例，在Ribbon中会默认采用轮询的方式进行调用，从而实现客户端的负载均衡。



#### 服务下线

> 在系统运行过程中必然会面临关闭或重启服务的某个实例情况，在服务关闭期间，我们自然不希望客户端会继续调用关闭了的实例。所以在客户端程序中，当服务实例进行正常的关闭操作时，它会触发一个服务下线的REST请求给Eureka Server，告诉服务注册中心：“我要下线了”。 服务端在接收到请求之后，将该服务状态置为下线（DOWN），并把下线事件传播出去。



### （3）注册中心

#### 失效剔除

> 有些时候，我们的服务实例并不一定会正常下线，可能由于内存溢出、网络故障灯原因使得服务不能正常工作，而服务注册中心并未收到“服务下线”的请求。为了从服务列表中将这些无法提供服务的实例剔除，Eureka Server在启动的时候会创建一个定时任务，默认每隔一段时间（默认60秒）将当前清单中超时（默认90秒）没有续约的服务剔除出去



#### 自我保护

上面已介绍自我保护如何使用。

> Eureka Server在运行期间，会统计心跳失败的比例在15分钟之内是否低于85%，如果出现低于的情况（在单机调试的时候很容易满足，实际生产环境上通常是由于网络不稳定导致），Eureka Server会将当前的实例注册信息保护起来，让这些实例不会过期，尽可能保护这些注册信息。但是，在这段保护期间内实例若出现问题，那么客户端很容易拿到实际已经不存在的服务实例，会出现调用失败的情况，所以客户端必须有容错机制，比如可以使用请求重试、断路器等机制



### 九、配置详解

Eureka的服务治理体系中，主要分为服务端和客户端两个角色，服务端为服务注册中心，而客户端为各个提供接口的微服务应用。

Eureka客户端配置主要分为下面两个方面：

- 服务注册相关配置信息，包含服务注册中心的地址、服务获取的间隔时间、可用区域。
- 服务实例相关的配置，包含服务实例的名称、ip地址、端口号、监控检查路径等等。

而Eureka服务端更多地类似一个现成产品，大多数情况下，我们不需要修改它的配置信息。





## 十、服务注册类配置

关于服务注册类的配置信息，可以通过查询`org.springframework.cloud.netflix.eureka.EurekaClientConfigBean`源码知晓。这些配置信息都以`eureka.client`为前缀。



| 参数名                                        | 说明                                                         | 默认值 |
| --------------------------------------------- | ------------------------------------------------------------ | ------ |
| enabled                                       | 启动Eureka客户端                                             | true   |
| registryFetchIntervalSeconds                  | 从Eureka服务端获取注册信息的间隔时间                         | 30     |
| instanceInfoReplicationIntervalSeconds        | 更新实例信息的变化到Eureka服务端的间隔时间                   | 30     |
| initialInstanceInfoReplicationIntervalSeconds | 初始化实例信息到Eureka服务端的间隔时间                       | 40     |
| eurekaServiceUrlPollIntervalSeconds           | 轮询Eureka服务端地址更改的间隔时间，当与Spring Cloud Config配合，动态刷新Eureka的serviceURL地址时  需要关注该参数 | 5 * 60 |
| eurekaServerReadTimeoutSeconds                | 读取Eureka Server信息的超时时间                              | 8      |
| eurekaServerConnectTimeoutSeconds             | 连接Eureka Server的超时时间                                  | 5      |
| eurekaServerTotalConnections                  | 从Eureka客户端到所有Eureka服务端的连接总数                   | 200    |
| eurekaServerTotalConnectionsPerHost           | 从Eureka客户端到每个Eureka服务端主机的连接总数               | 50     |
| eurekaConnectionIdleTimeoutSeconds            | Eureka服务端连接的空闲关闭时间                               | 30     |
| heartbeatExecutorThreadPoolSize               | 心跳连接池的初始化线程数                                     | 2      |
| heartbeatExecutorExponentialBackOffBound      | 心跳超时重试延迟时间的最大乘数值                             | 10     |
| cacheRefreshExecutorThreadPoolSize            | 缓存刷新线程池的初始化线程数                                 | 2      |
| cacheRefreshExecutorExponentialBackOffBound   | 缓存刷新重试延迟时间的最大乘数值                             | 10     |
| useDnsForFetchingServiceUrls                  | 使用DNS来获取Eureka服务端的serviceUrl                        | false  |
| registerWithEureka                            | 是否要将自身的实例信息注册到Eureka服务端                     | true   |
| preferSameZoneEureka                          | 是否偏好使用处于相同Zone的Eureka服务端                       | true   |
| filterOnlyUpInstances                         | 获取实例时是否过滤，仅保留UP状态实例                         | true   |
| fetchRegistry                                 | 是否从Eureka服务端获取注册信息                               | true   |



## 十一、服务实例类注册配置

关于服务实例类的配置信息，可以通过`org.springframework.cloud.netflix.eureka.EurekaInstanceConfigBean`源码来获取详细内容。 这些配置都以`eureka.instance`为前缀。

### 元数据

元数据是Eureka客户端在向服务注册中心发送注册请求时，用来描述自身服务信息的对象，其中包含了一些标准化的元数据，比如服务名称、实例名称、实例IP、实例端口等用于服务治理的重要信息；以及一些用于负载均衡策略或是其他特殊用途的自定义元数据信息。

`com.netflix.appinfo.InstanceInfo`类中详细定义了Ureka对元数据的定义，其中，Map<String,String> metadata是定义的元数据信息。自定义元数据，可以通过`eureka.instance.metadataMap.<key>=<value>`的格式来进行配置。如：`eureka.instance.metadataMap.zone=shanghai`



### 实例名配置

实例名是区分同一服务中不同实例的唯一标识。在Netflix Eureka的原生实现中，实例名采用主机名为默认值，这样的设置使得在同一主机上无法启动多个相同的服务实例。  所以，在Spring Cloud Eureka的配置汇总，针对同一主机中启动多实例的情况，对实例名的默认命名做了更加合理的扩展，它采用了如下默认规则：

```yaml
${spring.cloud.client.hostname}:${spring.application.name}:${spring.application.instance_id}:${server.port}
```

如果在本地进行客户端负载均衡调试时，需要启动同一服务的多个实例，直接启动同一个应用必然会产生端口冲突。需要可以指定server.port来启动，但是这样略显麻烦。对于这个问题，我们可以通过设置实例名规则(使用随机数`${random.int}`)解决，如：

```yaml
eureka.instance.instanceId=${spring.application.name}:${random.int}
```



### 其它配置

| 参数名                           | 说明                                                         | 默认值 |
| -------------------------------- | ------------------------------------------------------------ | ------ |
| preferIpAddress                  | 是否优先使用IP地址昨晚主机名的标识                           | false  |
| leaseRenewalIntervalInSeconds    | Eureka客户端向服务端发送心跳的时间间隔                       | 30     |
| leaseExpirationDurationInSeconds | Eureka服务端在接收到最后一次心跳之后等待的时间上限，超过该时间之后服务端会将该服务实例从服务清单中剔除，从而禁止服务调用请求被发送到改实例上 | 90     |
| nonSecurePort                    | 非安全的通讯端口号                                           | 80     |
| securePort                       | 安全的通讯端口号                                             | 443    |
| nonSecurePortEnabled             | 是否启用非安全的通讯端口号                                   | true   |
| securePortEnabled                | 是否启用安全的通讯端口号                                     |        |
| hostname                         | 主机名，不配置的时候将根据操作系统的主机名来获取             |        |



## 十二、跨平台支持

Eureka的通讯机制使用了**HTTP的REST接口实现**，这也是Eureka同其他服务注册工具的一个关键不同点。由于HTTP的平台无关系，虽然Eureka Server 通过java实现，但是在其下的微服务应用并不限于使用java来进行开发。跨平台本身就是微服务架构的九大特性之一，只有实现了对技术平台的透明，才能更好地发挥不同语言对不同业务处理能力的优势。

微服务的九大特性：

<https://www.jianshu.com/p/3d856716e3d5> 



























