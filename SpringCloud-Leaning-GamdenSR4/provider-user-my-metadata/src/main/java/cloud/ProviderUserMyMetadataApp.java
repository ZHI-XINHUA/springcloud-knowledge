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
@EnableDiscoveryClient //或 @EnableEurekaClient
public class ProviderUserMyMetadataApp
{
    public static void main( String[] args )
    {
        SpringApplication.run(ProviderUserMyMetadataApp.class,args);
    }
}
