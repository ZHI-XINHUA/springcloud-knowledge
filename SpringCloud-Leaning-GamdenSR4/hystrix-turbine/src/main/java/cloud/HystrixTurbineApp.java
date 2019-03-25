package cloud;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.turbine.EnableTurbine;


/**
 * HystrixDashboard 服务
 */
@SpringBootApplication
@EnableTurbine
public class HystrixTurbineApp
{
    public static void main( String[] args )
    {
        SpringApplication.run(HystrixTurbineApp.class,args);
    }
}
