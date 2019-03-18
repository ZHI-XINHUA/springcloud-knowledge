package cloud;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;


/**
 * Hello world!
 *
 */
@SpringBootApplication
//@EnableEurekaClient
@EnableDiscoveryClient
public class ProviderUserWithAuthApp
{
    public static void main( String[] args )
    {
        SpringApplication.run(ProviderUserWithAuthApp.class,args);
    }
}
