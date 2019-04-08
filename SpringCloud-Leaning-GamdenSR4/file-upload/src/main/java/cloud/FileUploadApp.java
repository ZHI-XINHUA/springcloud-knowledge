package cloud;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;

/**
 * 启动类
 *
 */
@EnableDiscoveryClient
@SpringBootApplication
public class FileUploadApp
{

    public static void main( String[] args )
    {
        SpringApplication.run(FileUploadApp.class,args);
    }
}
