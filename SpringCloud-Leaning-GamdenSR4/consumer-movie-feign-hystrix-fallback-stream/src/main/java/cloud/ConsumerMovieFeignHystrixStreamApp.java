package cloud;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import org.springframework.cloud.client.circuitbreaker.EnableCircuitBreaker;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.netflix.feign.EnableFeignClients;

/**
 * Hello world!
 *
 */
@SpringBootApplication
@EnableEurekaClient
@EnableFeignClients //开启Feign
@EnableCircuitBreaker //开启断路器
public class ConsumerMovieFeignHystrixStreamApp
{

    public static void main( String[] args )
    {
        SpringApplication.run(ConsumerMovieFeignHystrixStreamApp.class,args);
    }
}
