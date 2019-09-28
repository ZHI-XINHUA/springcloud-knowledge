package configure;

import cloud.mylog.InfoFeignLoggerFactory;
import feign.*;
import feign.auth.BasicAuthRequestInterceptor;
import org.springframework.cloud.netflix.feign.FeignLoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 不应该在应用上下文@ComponentScan之中，不然会被程序共享
 */
@Configuration
public class FeignConfigure {

    /**
     * 将契约改为feign原生的默认契约。这样就可以使用feign自带的注解了。
     * @return 默认的feign契约
     */
    @Bean
    public Contract feignContract() {
        return new feign.Contract.Default();
    }

    /**
     * 基于HTTP Basic认证
     * @return
     */
    /*@Bean
    public BasicAuthRequestInterceptor basicAuthRequestInterceptor() {
        return new BasicAuthRequestInterceptor("user", "password");
    }*/

    /**
     * 自定义过滤器
     * @return
     */
    @Bean
    public RequestInterceptor requestInterceptor(){
        return new RequestInterceptor(){
            @Override
            public void apply(RequestTemplate requestTemplate) {
                String url = requestTemplate.url();
                System.out.println("请求url: "+url);
            }
        };
    }

    /**
     * 定义日志的级别
     * @return
     */
    @Bean
    Logger.Level feignLevel() {
        return Logger.Level.FULL;
    }

    /**
     * 自定义日志
     * @return
     */
    @Bean
    FeignLoggerFactory infoFeignLoggerFactory() {
        return new InfoFeignLoggerFactory();
    }

}
