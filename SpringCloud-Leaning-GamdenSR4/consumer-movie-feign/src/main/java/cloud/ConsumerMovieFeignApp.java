package cloud;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.netflix.feign.EnableFeignClients;

/**
 * Hello world!
 *
 */
@SpringBootApplication
@EnableEurekaClient
@EnableFeignClients //开启Feign
public class ConsumerMovieFeignApp
{

    public static void main( String[] args )
    {
        SpringApplication.run(ConsumerMovieFeignApp.class,args);
    }
}
