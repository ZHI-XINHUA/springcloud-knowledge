package com.zxh.feignserver.fallback;

import com.zxh.feignserver.UserFeignClient;
import com.zxh.vo.UserVO;
import org.springframework.stereotype.Component;

/**
 * @ClassName UserFeignClientFallback
 * @Description 服务降级 回退类
 * @Author xh.zhi
 * @Date 2019-9-27 13:55
 * @Version 1.0
 **/
@Component
public class UserFeignClientFallback implements UserFeignClient {
    @Override
    public UserVO getUser(String userId) {
        UserVO userVO = new UserVO();
        userVO.setUsername("用户不存在！");
        return userVO;
    }

    @Override
    public String sayHi(String name) {
        return  "error";
    }

    @Override
    public String sayHi(String name, int age) {
        return "error";
    }

    @Override
    public String sayHi(UserVO user) {
        return  "error";
    }
}
