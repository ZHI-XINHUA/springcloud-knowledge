package cloud;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;

/**
 * Hello world!
 *
 */
@EnableDiscoveryClient
@SpringBootApplication
public class ConsumerMovieApp
{
    @Bean
    @LoadBalanced  //添加此注解后，RestTemplate整合Ribbon，使其具备负载均衡的能力
    public RestTemplate restTemplate(){
        return new RestTemplate();
    }
    public static void main( String[] args )
    {
        SpringApplication.run(ConsumerMovieApp.class,args);
    }
}
