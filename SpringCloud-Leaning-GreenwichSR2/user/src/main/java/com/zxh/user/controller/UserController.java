package com.zxh.user.controller;

import com.zxh.user.entity.User;
import com.zxh.user.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @ClassName UserController
 * @Description 用户controller
 * @Author xh.zhi
 * @Date 2019-9-26 12:06
 * @Version 1.0
 **/
@RestController
@RequestMapping("/user")
@Slf4j
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping("/{userId}")
    public User getUserById(@PathVariable  String userId){
        log.info("UserController!getUserById>>>>>>>获取用户");
        return userService.getUserById(userId);
    }
}
