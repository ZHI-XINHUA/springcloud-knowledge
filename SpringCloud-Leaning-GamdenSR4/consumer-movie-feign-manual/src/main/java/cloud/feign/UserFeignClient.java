package cloud.feign;

import cloud.entity.User;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 *feign 接口，调用provider-user服务
 */
public interface UserFeignClient {

    @RequestMapping(value = "/user/{userId}",method = RequestMethod.GET)
    User findUser(@PathVariable("userId") String userId); //坑1：注意@PathVariable 要添加value值

}
