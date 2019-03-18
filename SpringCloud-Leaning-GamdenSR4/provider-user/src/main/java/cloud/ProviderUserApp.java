package cloud;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;


/**
 * Hello world!
 *
 */
@SpringBootApplication
//@EnableEurekaClient
@EnableDiscoveryClient
public class ProviderUserApp
{
    public static void main( String[] args )
    {
        SpringApplication.run(ProviderUserApp.class,args);
    }
}
