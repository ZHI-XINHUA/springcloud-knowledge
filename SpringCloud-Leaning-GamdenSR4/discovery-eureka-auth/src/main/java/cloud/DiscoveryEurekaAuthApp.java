package cloud;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;

/**
 * Hello world!
 *
 */
@SpringBootApplication
@EnableEurekaServer //@EnableDiscoveryClient
public class DiscoveryEurekaAuthApp
{
    public static void main( String[] args )
    {
        SpringApplication.run(DiscoveryEurekaAuthApp.class,args);
    }
}
