package com.zxh.controller;


import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.zxh.feignserver.UserFeignClient;
import com.zxh.vo.UserVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


/**
 * @ClassName MovieController
 * @Description
 * @Author xh.zhi
 * @Date 2019-9-26 14:12
 * @Version 1.0
 **/
@RestController
@RequestMapping("/movie")
@Slf4j
public class MovieController {

    @Autowired
    UserFeignClient userFeignClient;



    @GetMapping("/{userId}")
    //@HystrixCommand(fallbackMethod ="findUserError" )
    public UserVO findUser(@PathVariable String userId){
        log.info("MovieController!findUser-->feign访问服务");
        return  userFeignClient.getUser(userId);
    }

  /*  public UserVO findUserError(String userId){
        log.info("MovieController!findUserError-->feign访问服务失败！");
        UserVO userVO = new UserVO();
        userVO.setUsername("用户不存在！");
        return userVO;
    }*/

    @GetMapping("/say1")
    public String say1(){
        return userFeignClient.sayHi("zxh");
    }

    @GetMapping("/say2")
    public String say2(){
        return userFeignClient.sayHi("zxh",20);
    }

    @GetMapping("/say3")
    public String say3(){
        UserVO user = new UserVO();
        user.setUsername("zxh");
        user.setEmail("1960881192@qq.com");
        return userFeignClient.sayHi(user);
    }


}
