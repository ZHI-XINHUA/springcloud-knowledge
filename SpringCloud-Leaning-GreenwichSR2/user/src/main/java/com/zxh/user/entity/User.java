package com.zxh.user.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;



import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;
import java.sql.Date;

/**
 * @ClassName User
 * @Description TODO
 * @Author xh.zhi
 * @Date 2019-9-26 11:53
 * @Version 1.0
 **/

@Data
@Entity
@Table(name = "user")
public class User implements Serializable {

    @Id
    private String userid;

    private String usercode;

    private String username;

    @JsonIgnore
    private String password;

    private String email;

    private String telephone;

    private Date birthday;

    private String sex;

    private Integer state;
}
