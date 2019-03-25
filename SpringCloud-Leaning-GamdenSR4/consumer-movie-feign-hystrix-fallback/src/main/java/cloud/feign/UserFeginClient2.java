package cloud.feign;

import cloud.entity.User;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * 使用fallbackFactory实现容错
 */
@FeignClient(name = "provider-user",fallbackFactory =FeginClientFallbackFactory.class )
public interface UserFeginClient2 {
    @RequestMapping(value = "/user/{userId}",method = RequestMethod.GET)
    User findUser(@PathVariable("userId") String userId); //坑1：注意@PathVariable 要添加value值
}
