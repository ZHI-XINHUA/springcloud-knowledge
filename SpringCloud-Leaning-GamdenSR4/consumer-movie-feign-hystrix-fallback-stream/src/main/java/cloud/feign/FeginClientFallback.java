package cloud.feign;

import cloud.entity.User;
import org.springframework.stereotype.Component;

/**
 * 回退类FeginClientFallBack需要实现Fegin Client接口
 */
@Component
public class FeginClientFallback implements  UserFeignClient{

    @Override
    public User findUser(String userId) {
        User user = new User();
        user.setUserId("-1");
        user.setUserCode("-1");
        user.setUserName("Fegin fallback --> 找不到用户");
        return  user;
    }
}