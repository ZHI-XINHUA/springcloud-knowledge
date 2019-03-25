package cloud.feign;

import cloud.entity.User;
import feign.hystrix.FallbackFactory;
import org.springframework.stereotype.Component;


/**
 * fallback 容错工厂类，实现FallbackFactory接口，并覆写create方法（实现Fegin接口的方法）
 */
@Component
public class FeginClientFallbackFactory implements FallbackFactory<UserFeginClient2> {

    @Override
    public UserFeginClient2 create(Throwable throwable) {
        //简单打印错误信息
        //Fegin bug: throwable 可能为null
        // 在Spring Cloud Gamden SR4使用的Fegin版本是9.3.1. Fegin 9.4.0才修改修改throwabel为null的bug
        System.out.println(throwable.getMessage());

        //实现Fegin接口，返回容错的信息
        return new UserFeginClient2(){

            @Override
            public User findUser(String userId) {
                User user = new User();
                user.setUserId("-1");
                user.setUserCode("-1");
                user.setUserName("Fegin fallbackFactory --> 找不到用户");
                return  user;
            }
        };


    }
}
