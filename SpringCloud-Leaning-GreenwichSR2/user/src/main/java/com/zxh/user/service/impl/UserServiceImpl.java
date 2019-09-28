package com.zxh.user.service.impl;

import com.zxh.user.entity.User;
import com.zxh.user.repository.UserRepository;
import com.zxh.user.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @ClassName UserServiceImpl
 * @Description 用户service
 * @Author xh.zhi
 * @Date 2019-9-26 12:05
 * @Version 1.0
 **/
@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public User getUserById(String userId) {
        return userRepository.findById(userId).get();
    }
}
