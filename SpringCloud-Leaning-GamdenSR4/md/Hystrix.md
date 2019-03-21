# Hystrix 实现微服务的容错处理

微服务架构的应用系统通常包含过个服务层。微服务之间通过网络进行通信，从而支持起整个应用系统。任何微服务都并非100%可用，网络往往很脆弱，因此难免有些请求会失败。

为了防止雪崩效应，Hystrix提供了容错机制。 容错机制需要实现以下两点：

1. **为网络请求设置超时**

   必须为网络请求设置超时。如果依赖服务不可用或者网络有问题，响应时间就会变得很长。

   一次远程调用对应着一个线程/进程，如果响应太慢，这个线程/进程就得不到释放，资源就会逐渐被耗尽，最终导致服务的不可用。

   

2. **使用断路器模式**

如果对某个微服务的请求有大量超时（微服务不可用或网络有问题），再去让新的请求访问改服务已经没有任何意义，只会无谓消耗资源。

断路器可理解为对容易导致错误的操作的代理。这种代理能够统计一段时间内调用失败的次数，并决定是正常请求依赖服务还是直接返回。

**断路器可以实现快速失败**，如果它在一段时间内检测到许多类似的错误（如超时等），就会在之后的一段时间内，强迫对该服务的调用快速失败，即不在请求所依赖的服务。这样，应用程序就无需再浪费CPU时间去等待长时间的超时。

**断路器也可以自动诊断服务是否已经恢复正常**。如果发现依赖的服务已经恢复正常，那么就会恢复请求该服务。



断路器状态转换逻辑：

- 正常情况下，断路器关闭，可正常请求依赖的服务。
- 当一段时间内，请求失败率达到一定阀值（如错误率达到50%，或100次/分钟等），断路器就会打开。此时，不会再去请求依赖的服务。
- 断路器打开一段时间后，会自动进入“半开”状态。此时，断路器允许一个请求访问依赖的服务。如果该请求能够成功，则关闭断路器；否则继续保持打开状态。



## Hystrix 简介

Hystrix是由Netflix开源的一个延迟和容错库，用于隔离访问远程系统、服务或者第三方库，防止级联失败，从而提升系统的可用性与容错性。Hystrix主要通过以下几点实现延迟和容错。

- 包裹请求：使用`HystrixCommand`(或`HystrixObservableCommand`)包裹对依赖的调用逻辑，每个命令在独立线程中执行。使用到了设计模式中的“命令模式”

- 跳闸机制：当某服务的错误率超过一定阀值时，Hystrix可以自动或手动跳闸，停止请求该服务一段时间。

- 资源隔离：Hystrix为每个依赖都维护了一个小型的线程池（或信号量）。如果该线程池已满，发往该依赖的请求就被立即拒绝，而不是排队等候，从而加速失败判定。

- 监控：Hystrix可以近乎实时地监控运行指标和配置的变化，例如成功、失败、超时、以及被拒绝的请求等。

- 回退机制：当请求失败、超时、被拒绝，或当断路器打开是，执行会逻辑（由开发人员执行提供），例如返回一个缺省值。

- 自我修复：断路器打开一段时间后，会自动进入“半开”状态。断路器打开、关闭、半开的逻辑转换。

  

## 一、简单使用Hystrix

项目demo：`consumer-movie-ribbon-hystrix`

### 1、添加 hystrix依赖

```xml
<!--添加 hystrix-->
<dependency>
    <groupId>org.springframework.cloud</groupId>
    <artifactId>spring-cloud-starter-hystrix</artifactId>
</dependency>
```



### 2、启动类中开启Hystrix(添加`@EnableCircuitBreaker`或`@EnableHystrix`)

```java
@EnableDiscoveryClient
@SpringBootApplication
@EnableCircuitBreaker //开启hystrix 或 @EnableHystrix
public class ConsumerMovieApp
{
    @Bean
    @LoadBalanced
    public RestTemplate restTemplate(){
        return new RestTemplate();
    }
    public static void main( String[] args )
    {
        SpringApplication.run(ConsumerMovieApp.class,args);
    }
}

```



### 3、controller中findUser方法添加容错能力

```java
@RestController
@RequestMapping("/movie")
public class MovieController {


    @Autowired
    private RestTemplate restTemplate;



    @HystrixCommand(fallbackMethod = "findUserFallback") //指定容错的方法
    @GetMapping("/{userId}")
    public User findUser(@PathVariable  String userId){
      return  restTemplate.getForObject("http://provider-user/user/"+userId,User.class);
    }

    /**
     * findUser 的容错方法，返回值和参数与findUser方法一致
     * @param userId
     * @return
     */
    public User findUserFallback(String userId){
        User user = new User();
        user.setUserId("-1");
        user.setUserCode("-1");
        user.setUserName("找不到用户");
        return  user;
    }
}

```

`findUserFallback`方法是`findUser`的回退方法，回退方法和`findUser`必选**具有相同的参数与返回值类型**



当请求失败、被拒绝、超时或者断路器打开时，都会进入回退方法。



## 二、Actuator监控Hystrix

### 1、添加Actuator依赖

```xml
 <dependency>
     <groupId>org.springframework.boot</groupId>
     <artifactId>spring-boot-starter-actuator</artifactId>
</dependency>
```



重新启动，断路器的状态也会暴露在Actuator提供的/health端点中。

断路器关闭的时候，访问http://localhost:6001/health 结果如下

```xml
{
	...
	"hystrix": {
		"status": "UP"
	}
}
```

Hystrix状态为UP，也就是一切正常，此时断路器是关闭的。



停止provider-user服务后，Hystrix状态信息为

```xml
{
...
"hystrix": {
		"status": "CIRCUIT_OPEN",
		"openCircuitBreakers": ["MovieController::findUser"]
	}
}
```



























