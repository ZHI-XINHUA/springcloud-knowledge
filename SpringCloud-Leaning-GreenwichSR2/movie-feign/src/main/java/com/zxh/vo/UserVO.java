package com.zxh.vo;

import lombok.Data;

import java.sql.Date;

/**
 * @ClassName User
 * @Description TODO
 * @Author xh.zhi
 * @Date 2019-9-26 14:11
 * @Version 1.0
 **/
@Data
public class UserVO {
    private String userid;

    private String usercode;

    private String username;

    private String password;

    private String email;

    private String telephone;

    private Date birthday;

    private String sex;

    private Integer state;
}
