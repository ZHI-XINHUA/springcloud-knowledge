package com.zxh.controller;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.zxh.vo.UserVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

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
    private RestTemplate restTemplate;

    @GetMapping("/{userId}")
    @HystrixCommand(fallbackMethod ="findUserError" )
    public UserVO findUser(@PathVariable String userId){
        log.info("MovieController!findUser-->restTemplate访问服务");
        String url ="http://PROVIDER-USER/user/"+userId;
        return restTemplate.getForObject(url,UserVO.class);
    }

    public UserVO findUserError(String userId){
        log.info("MovieController!findUserError-->restTemplate访问服务失败！");
        UserVO userVO = new UserVO();
        userVO.setUsername("用户不存在！");
        return userVO;
    }
}
