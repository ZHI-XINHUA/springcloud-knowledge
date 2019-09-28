package com.zxh.user.service;

import com.zxh.user.entity.User;
import org.springframework.stereotype.Service;


public interface UserService {
    /**
     * 根据ID获取用户
     * @param userId
     * @return
     */
    User getUserById(String userId);
}
