package cloud;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.hystrix.dashboard.EnableHystrixDashboard;


/**
 * HystrixDashboard 服务
 */
@SpringBootApplication
@EnableHystrixDashboard //开启dashboard
public class HystrixDashboardApp
{
    public static void main( String[] args )
    {
        SpringApplication.run(HystrixDashboardApp.class,args);
    }
}
