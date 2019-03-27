package cloud;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.turbine.stream.EnableTurbineStream;


/**
 * HystrixDashboard 服务
 */
@SpringBootApplication
@EnableTurbineStream
public class HystrixTurbineMqApp
{
    public static void main( String[] args )
    {
        SpringApplication.run(HystrixTurbineMqApp.class,args);
    }
}
