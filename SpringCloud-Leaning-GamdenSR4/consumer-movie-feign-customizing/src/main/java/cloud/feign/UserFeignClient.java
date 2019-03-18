package cloud.feign;

import cloud.entity.User;
import configure.FeignConfigure;
import feign.Param;
import feign.RequestLine;
import org.springframework.cloud.netflix.feign.FeignClient;


/**
 *feign 接口，调用provider-user服务,指定自定义Feign配置FeignConfigure
 */
@FeignClient(name = "provider-user",configuration = FeignConfigure.class)
public interface UserFeignClient {

    //使用feign自带的注解@RequestLine
    @RequestLine("GET /user/{userId}")
    User findUser(@Param("userId") String userId);

}
