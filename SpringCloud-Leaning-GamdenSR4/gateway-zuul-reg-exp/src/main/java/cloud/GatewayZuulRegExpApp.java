package cloud;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.zuul.EnableZuulProxy;
import org.springframework.cloud.netflix.zuul.filters.discovery.PatternServiceRouteMapper;
import org.springframework.context.annotation.Bean;

/**
 * 启动类
 *
 */
@SpringBootApplication
@EnableZuulProxy  //声明一个Zuul代理，该代理使用Ribbon来定位注册在Eureka Server中的微服务；同时还整合了Hystrix，从而实现类容错，
//所有经过Zuul的请求都会在Hystrix命令中执行
public class GatewayZuulRegExpApp
{
    public static void main( String[] args )
    {
        SpringApplication.run(GatewayZuulRegExpApp.class,args);
    }

    @Bean
    public PatternServiceRouteMapper serviceRouteMapper(){
        //PatternServiceRouteMapper 构造器：servicePattern：指定微服务的正则  routePattern：指定路由正则
        return new PatternServiceRouteMapper("(?<name>^.+)-(?<version>v.+$)", "${version}/${name}");
        //return new PatternServiceRouteMapper("(?<name>^.+)-(?<version>v.+$)","${version/${name}}");

    }
}
