package cloud;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;

/**
 * Hello world!
 *  Eureka Server 高可用
 */
@SpringBootApplication
@EnableEurekaServer
public class EurekaServerHaApp
{
    public static void main( String[] args )
    {
        SpringApplication.run(EurekaServerHaApp.class,args);
    }
}
